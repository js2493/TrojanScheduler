import dataclasses


@dataclasses.dataclass
class Section:
    section: str = ""
    session: str = ""
    type: str = ""
    start_time: int = 0
    end_time: int = 0
    days: int = 0
    registered: str = ""
    instructor: str = ""
    location: str = ""

    # instructor_link: str = ""
    # syllabus: str = ""
    # info: str = ""

    def to_dict(self):
        return {
            "section": self.section,
            "session": self.session,
            "type": self.type,
            "start_time": self.start_time,
            "end_time": self.end_time,
            "days": self.days,
            "registered": self.registered,
            "instructor": self.instructor,
            "location": self.location
        }

    def __str__(self):
        # return str(self.to_dict())
        return f"{self.section} {self.session} {self.type} {self.start_time} {self.end_time} {self.days} {self.registered} {self.instructor} {self.location}"


