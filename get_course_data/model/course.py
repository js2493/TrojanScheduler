import dataclasses
from typing import List

from model.section import Section

@dataclasses.dataclass
class Course:
    department: str = ""
    course_number: str = ""
    course_name: str = ""
    units: str = ""
    description: str = ""
    term: str = ""
    sections: List[Section] = dataclasses.field(default_factory=list)

    def to_dict(self):
        return {
            "term": self.term,
            "department": self.department,
            "course_number": self.course_number,
            "course_name": self.course_name,
            "units": self.units,
            "description": self.description,
            "sections": [section.to_dict() for section in self.sections]
        }

    def __str__(self):
        return f"{self.term} {self.department}-{self.course_number}: {self.course_name} {self.units}" + \
                f"\n\t{self.description}" + str(self.sections)



