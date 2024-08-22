import sys

import requests
from bs4 import BeautifulSoup

from repository.school_department_repository import SchoolDepartmentRepository

term = None
if len(sys.argv) > 1:
    term = sys.argv[1]

while term is None or "-" not in term or len(term) != 6:
    print("Invalid term, Enter in format of YYYY-T where T is 1, 2, or 3")
    term = input("Enter valid term: ")

year, semester = term.split("-")

url = f"https://classes.usc.edu/term-{year}{semester}/"
response = requests.get(url)
if response.status_code == 404:
    print("404 - Page Not Found")
    sys.exit(0)

soup = BeautifulSoup(response.content, "html.parser")
soup = soup.find(id="sortable-classes")
schools = soup.find_all(attrs={'data-type': "school"})
school_dict = {}
for school in schools:
    school_dict[school.get("data-school")] = school.find("a").text.strip()

departments_dict = {school: [] for school in school_dict.values()}
departments = soup.find_all(attrs={'data-type': "department"})
for department in departments:
    key = department.get("data-school")
    if key in school_dict:
        departments_dict[school_dict[key]].append([department.get("data-title"), department.get("data-code")])

school_dep_repo = SchoolDepartmentRepository()
school_dep_repo.batch_insert(departments_dict)



