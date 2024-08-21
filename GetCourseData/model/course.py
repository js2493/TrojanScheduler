import dataclasses
from typing import List

from model.section import Section

@dataclasses.dataclass
class Course:
    department: str = ""
    course_num: str = ""
    course_name: str = ""
    units: str = ""
    description: str = ""
    sections: List[Section] = dataclasses.field(default_factory=list)

    def to_dict(self):
        return {
            "department": self.department,
            "course_num": self.course_num,
            "course_name": self.course_name,
            "units": self.units,
            "description": self.description,
            "sections": [section.to_dict() for section in self.sections]
        }

    def __str__(self):
        return f"{self.department}-{self.course_num}: {self.course_name} {self.units}" + \
                f"\n\t{self.description}" + str(self.sections)



