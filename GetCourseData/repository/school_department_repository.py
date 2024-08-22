from repository.repository import PostgreRepository


class SchoolDepartmentRepository(PostgreRepository):
    insert_sql = """
    INSERT INTO school_department (school, department, code)
    VALUES (%s, %s, %s)
    ON CONFLICT (code)
    DO NOTHING;
    """

    def create_table(self):
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS school_department (
            id SERIAL PRIMARY KEY,
            school TEXT NOT NULL,
            department TEXT NOT NULL,
            code TEXT NOT NULL UNIQUE
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()

        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def batch_insert(self, school_dict):
        try:
            for school, departments in school_dict.items():
                for department in departments:
                    self.cursor.execute(self.insert_sql, (
                        school,
                        department[0],
                        department[1]
                    ))
            self.connection.commit()
        except Exception as e:
            print(f"An error occurred while inserting the course: {e}")
            self.connection.rollback()
