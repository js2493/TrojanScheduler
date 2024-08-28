# # from model.course import Course
# #
# # myCourse = Course()
# # print(myCourse)
# #
# # setattr(myCourse, 'units', 5)
# #
# #
# #
# #
# # from pathlib import Path
# #
# # directory_path = Path("jsons/2024/3")
# # files = [f.name for f in directory_path.iterdir() if f.is_file()]
# # print(files)
# import psycopg2
# from dotenv import load_dotenv
# import os
#
# load_dotenv()
# connection = psycopg2.connect(
#     dbname=os.environ.get('DB_NAME'),
#     user=os.environ.get('DB_USER'),
#     password=os.environ.get('DB_PASS'),
#     host=os.environ.get('DB_HOST')
# )
# cursor = connection.cursor()
#
# from utility import day_utility, time_utility
#
# sql = """
# SELECT c.department, c.course_num, c.course_name, s.start_time, s.end_time, s.days
# FROM courses c
# JOIN sections s ON s.course_id = c.id
# WHERE (c.department = 'CSCI' and c.course_num = '402' and s.location = 'DEN@Viterbi')
# OR (c.department = 'CSCI' and c.course_num = '566')
# OR (c.department = 'EE' and c.course_num = '450' and s.location = 'DEN@Viterbi')
# """
# # WHERE (c.department = 'CSCI' or c.department = 'EE') AND s.location = 'DEN@Viterbi'
#
#
# cursor.execute(sql)
# sections = cursor.fetchall()
# for s in sections:
#     print(f"{s[0].ljust(5)} {s[1].ljust(4)} {s[2].ljust(40)} {day_utility.decode_days(s[5])} {time_utility.decode_time(s[3], s[4])}")
#

x = 1
for _ in range(10):
    y = (1 - 1/x) ** x
    print(y)
    x *= 10
