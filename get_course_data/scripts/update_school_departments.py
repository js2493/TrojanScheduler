import sys

import requests
from bs4 import BeautifulSoup

from repository.department_repository import DepartmentRepository
from repository.school_repository import SchoolRepository
from model.department import Department

term = None
if len(sys.argv) > 1:
    term = sys.argv[1]

while term is None or "-" not in term or len(term) != 6:
    print("Invalid term, Enter in format of YYYY-T where T is 1, 2, or 3")
    term = input("Enter valid term: ")

year, semester = term.split("-")
print("getting response")
url = f"https://classes.usc.edu/term-{year}{semester}/"
response = requests.get(url)
if response.status_code == 404:
    print("404 - Page Not Found")
    sys.exit(0)

print("parsing")

soup = BeautifulSoup(response.content, "html.parser")
soup = soup.find(id="sortable-classes")
schools = soup.find_all(attrs={'data-type': "school"})
school_dict = {}
for school in schools:
    school_dict[school.get("data-school").strip()] = school.find("a").text.strip()

print("getting data")

departments_dict = {school: [] for school in school_dict.keys()}
departments = soup.find_all(attrs={'data-type': "department"})
for department in departments:
    key = department.get("data-school").strip()
    if key in school_dict:
        departments_dict[key].append(Department(
            code=department.get("data-code"), department=department.get("data-title")))
        print("key", department.get("data-code"))



print("uploading data")
school_ids = {}
school_names = [name for name in school_dict.keys()]

school_repo = SchoolRepository()
for school_name in school_names:
    print(f"{school_name}|{school_dict[school_name]}")
    school_id = school_repo.insert(school_dict[school_name])
    school_ids[school_name] = school_id
school_repo.close()

dep_repo = DepartmentRepository()
for school_name in school_names:
    school_id = school_ids[school_name]
    dep_repo.batch_insert(departments_dict[school_name], school_id)
dep_repo.close()








