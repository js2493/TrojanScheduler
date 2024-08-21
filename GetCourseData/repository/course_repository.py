from repository.repository import PostgreRepository

class CourseRepository(PostgreRepository):

    def create_table(self):
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS courses (
            id SERIAL PRIMARY KEY,
            department VARCHAR(20) NOT NULL,
            course_num VARCHAR(20) NOT NULL,
            course_name VARCHAR(255) NOT NULL,
            units VARCHAR(30) NOT NULL,
            description VARCHAR(1000),
            term VARCHAR(20) NOT NULL
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()

        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()


    def insert(self, course):
        insert_course_sql = """
        INSERT INTO courses (term, department, course_num, course_name, units, description)
        VALUES (%s, %s, %s, %s, %s, %s) RETURNING id;
        """
        try:
            self.cursor.execute(insert_course_sql, (
                course.term,
                course.department,
                course.course_num,
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

    def batch_insert(self, courses):
        insert_course_sql = """
        INSERT INTO courses (term, department, course_num, course_name, units, description)
        VALUES (%s, %s, %s, %s, %s, %s) RETURNING id;
        """
        course_ids = []
        try:
            for course in courses:
                self.cursor.execute(insert_course_sql, (
                    course.term,
                    course.department,
                    course.course_num,
                    course.course_name,
                    course.units,
                    course.description
                ))
                course_ids.append(self.cursor.fetchone()[0])
            self.connection.commit()
            return course_ids
        except Exception as e:
            print(f"An error occurred while inserting the course: {e}")
            self.connection.rollback()
            return None

