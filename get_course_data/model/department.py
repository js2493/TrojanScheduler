import dataclasses


@dataclasses.dataclass
class Department:
    code: str = ""
    department: str = ""
    school_id: int = 0
