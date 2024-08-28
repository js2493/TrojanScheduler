import json

import requests
from bs4 import BeautifulSoup
import sys
from pathlib import Path

from model.course import Course
from model.section import Section
from utility import time_utility, day_utility

term = None
if len(sys.argv) > 1:
    term = sys.argv[1]

while term is None or "-" not in term or len(term) != 6:
    print("Invalid term, Enter in format of YYYY-T where T is 1, 2, or 3")
    term = input("Enter valid term: ")

year, semester = term.split("-")

year_path = Path("../jsons") / year

if not year_path.exists():
    year_path.mkdir(parents=True, exist_ok=True)

semester_path = year_path / semester

if not semester_path.exists():
    semester_path.mkdir(parents=True, exist_ok=True)

url = f"https://classes.usc.edu/term-{year}{semester}/"
response = requests.get(url)
if response.status_code == 404:
    print("404 - Page Not Found")
    sys.exit(0)

soup = BeautifulSoup(response.content, "html.parser")
elements = soup.find_all(class_="prefix")
abbreviations = [element.text.lower() for element in elements]

for abbreviation in abbreviations:
    department_url = f"{url}classes/{abbreviation}/"
    response = requests.get(department_url)
    soup = BeautifulSoup(response.content, "html.parser")
    courses = soup.find_all(class_=["course-info", "expandable"])

    course_list = []
    for course in courses:
        link = course.select('.course-id h3 .courselink')[0]
        department, course_num = course.get('id').split("-")
        course_name = link.contents[-2].strip()
        units = link.contents[-1].text
        details = course.select('.course-details')[0]
        desc = details.find(class_="catalogue").text.strip()

        sections = details.find(class_=["sections", "responsive"]).find_all('tr')
        headers = [header.get('class')[0] for header in sections[0].find_all('th')]

        course_sections = []
        for section in sections[1:]:
            if not section.has_attr('data-section-id'):
                continue
            start_time = end_time = 0
            curr_section = Section()
            for header in headers:
                section_data = section.find(class_=header)
                section_data = None if not section_data else section_data.text.strip()

                if not section_data:
                    continue

                if header == "time":
                    start_time, end_time = time_utility.encode_time(section_data)
                    continue

                if header == "days":
                    day_val = day_utility.encode_days(section_data)
                    section_data = day_val

                setattr(curr_section, header, section_data)

            curr_section.start_time = start_time
            curr_section.end_time = end_time
            course_sections.append(curr_section)

        course_list.append(
            Course(term=year + semester, department=department, course_number=course_num, course_name=course_name,
                   units=units, description=desc, sections=course_sections))

    with open(f'../jsons/{year}/{semester}/{abbreviation}.json', 'w') as file:
        json.dump([course.to_dict() for course in course_list], file, indent=4)

    print("finished with " + abbreviation.upper())
