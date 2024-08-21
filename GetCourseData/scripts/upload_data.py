import subprocess
import sys
from pathlib import Path

term = None
if len(sys.argv) > 1:
    term = sys.argv[1]

while term is None or "-" not in term or len(term) != 6:
    print("Invalid term, Enter in format of YYYY-T where T is 1, 2, or 3")
    term = input("Enter valid term: ")

year, semester = term.split("-")

path = Path("../jsons") / year / semester

if not path.exists():
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

