import json

import requests
from bs4 import BeautifulSoup

from model.section import Section
from utility import time_utility, day_utility


from model.course import Course

# url = "https://classes.usc.edu/term-20243/"
# response = requests.get(url)
#
# soup = BeautifulSoup(response.content, "html.parser")
#
# elements = soup.find_all(class_="prefix")
#
# for element in elements:
#     print(element.text)
#
# with open("csci.htm", 'r') as file:
#     html_content = file.read()
#
# soup = BeautifulSoup(html_content, "html.parser")

url = "https://classes.usc.edu/term-20243/classes/arch"
response = requests.get(url)
soup = BeautifulSoup(response.content, "html.parser")

elements = soup.find_all(class_=["course-info", "expandable"])

courses = []
for element in elements:
    link = element.select('.course-id h3 .courselink')[0]
    # department, course_num = element.get('id').split("-")
    department, course_num = link.contents[-3].text.strip()[:-1].split(" ")
    course_name = link.contents[-2].strip()
    units = link.contents[-1].text
    details = element.select('.course-details')[0]
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
                print(section_data)
                start_time, end_time = time_utility.encode_time(section_data)
                continue

            if header == "days":
                day_val = day_utility.encode_days(section_data)
                section_data = day_val
            setattr(curr_section, header, section_data)
        curr_section.start_time = start_time
        curr_section.end_time = end_time
        course_sections.append(curr_section)

    course = Course(department=department, course_num=course_num, course_name=course_name, units=units,
                    description=desc, sections=course_sections)
    courses.append(course)

with open('course.json', 'w') as file:
    json.dump([course.to_dict() for course in courses], file, indent=4)



