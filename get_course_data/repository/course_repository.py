from repository.repository import PostgreRepository


class CourseRepository(PostgreRepository):
    insert_sql = """
    INSERT INTO courses (term, department, course_number, course_name, units, description)
    VALUES (%s, %s, %s, %s, %s, %s)
    ON CONFLICT (term, department, course_number)
    DO NOTHING
    RETURNING id;
    """

    def create_table(self):
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS courses (
            id SERIAL PRIMARY KEY,
            department TEXT NOT NULL,
            course_number TEXT NOT NULL,
            course_name TEXT NOT NULL,
            units TEXT NOT NULL,
            description TEXT,
            term TEXT NOT NULL,
            FOREIGN KEY (department) REFERENCES school_department(code),
            UNIQUE(term, department, course_num)
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()

        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def insert(self, course):

        try:
            self.cursor.execute(self.insert_sql, (
                course.term,
                course.department,
                course.course_number,
                course.course_name,
                course.units,
                course.description
            ))
            course_id = self.cursor.fetchone()[0]
            self.connection.commit()
            return course_id
        except Exception as e:
            print(f"An error occurred while inserting the course: {e}")
            self.connection.rollback()
            return None

    # def batch_insert(self, courses):
    #     print("hello")
    #     existing_ids = self.get_existing_ids(courses)
    #     course_data = [(course.term, course.department, course.course_num,  course.course_name,
    #                     course.units, course.description) for course in courses]
    #     course_ids = []
    #     try:
    #         self.cursor.executemany(self.insert_sql, course_data)
    #         self.connection.commit()
    #         result = self.cursor.fetchall()
    #         for ind, row in enumerate(result):
    #             if row:
    #                 course_ids.append(row[0])
    #             else:
    #                 course_ids.append(existing_ids.get((course_data[ind][0], course_data[ind][1], course_data[ind][2])))
    #             print(course_ids)
    #         return course_ids
    #     except Exception as e:
    #         print(f"An error occurred while inserting the course: {e}")
    #         self.connection.rollback()

    def batch_insert(self, courses):
        existing_ids = self.get_existing_ids(courses)
        course_ids = []
        try:
            for course in courses:
                self.cursor.execute(self.insert_sql, (
                    course.term,
                    course.department,
                    course.course_number,
                    course.course_name,
                    course.units,
                    course.description
                ))
                result = self.cursor.fetchone()
                if not result:
                    result = [existing_ids.get((course.term, course.department, course.course_number))]
                course_ids.append(result[0])
            self.connection.commit()
            return course_ids
        except Exception as e:
            print(f"An error occurred while inserting the course: {e}")
            self.connection.rollback()
            return None


    def get_existing_ids(self, courses):
        existing_ids = {}
        for course in courses:
            self.cursor.execute("""
                   SELECT id FROM courses
                   WHERE term = %s AND department = %s AND course_number = %s
               """, (course.term, course.department, course.course_number))
            result = self.cursor.fetchone()
            if result:
                existing_ids[(course.term, course.department, course.course_number)] = result[0]
        return existing_ids
