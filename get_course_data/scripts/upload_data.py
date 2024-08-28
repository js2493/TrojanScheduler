import subprocess
import sys
from pathlib import Path

from repository.course_repository import CourseRepository
from repository.section_repository import SectionRepository
from utility import parse_course_json

term = None
if len(sys.argv) > 1:
    term = sys.argv[1]

while term is None or "-" not in term or len(term) != 6:
    print("Invalid term, Enter in format of YYYY-T where T is 1, 2, or 3")
    term = input("Enter valid term: ")

year, semester = term.split("-")

directory_path = Path("../jsons") / year / semester

if not directory_path.exists():
    response = input("Data does not exist yet. Download now? (Y/N): ")
    if response.strip().upper() == "Y":
        try:
            subprocess.run(["python3", "download_data.py", f"{year}-{semester}"], check=True)
            print("Data download finished.")
        except subprocess.CalledProcessError as e:
            print(f"An error occurred while running download_data.py: {e}")
            sys.exit(0)
    else:
        print("Data download aborted.")
        sys.exit(0)

files = [f.name for f in directory_path.iterdir() if f.is_file()]
course_repo = CourseRepository()
section_repo = SectionRepository()

i = 0
for file in files:
    i += 1
    courses, sections = parse_course_json.parse_data(str(directory_path) + "/" + file)
    course_ids = course_repo.batch_insert(courses)
    section_repo.batch_insert(sections, course_ids)
    print(f"{file.split('.')[0]}  {i}/{len(files)} finished")

course_repo.close()
section_repo.close()

