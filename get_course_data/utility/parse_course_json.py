import json

from model.course import Course
from model.section import Section


def parse_data(json_file_path):
    course_list = []
    section_list = []
    with open(json_file_path, 'r') as file:
        courses = json.load(file)
    for course in courses:
        curr_course = Course()
        curr_sections = []
        for key, value in course.items():
            if key != "sections":
                setattr(curr_course, key, value)
        for section in course["sections"]:
            curr_section = Section()
            for key, value in section.items():
                setattr(curr_section, key, value)
            curr_sections.append(curr_section)

        course_list.append(curr_course)
        section_list.append(curr_sections)

    return course_list, section_list






