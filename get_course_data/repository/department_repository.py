from repository.repository import PostgreRepository


class DepartmentRepository(PostgreRepository):
    insert_sql = """
    INSERT INTO departments (department, code, school_id)
    VALUES (%s, %s, %s)
    ON CONFLICT (code)
    DO NOTHING;
    """

    def create_table(self):
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS departments (
            id SERIAL PRIMARY KEY,
            department TEXT NOT NULL,
            code TEXT NOT NULL UNIQUE,
            school_id INT NOT NULL,
            FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
            UNIQUE (code)
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()

        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def batch_insert(self, departments, school_id):
        try:
            for department in departments:
                self.cursor.execute(self.insert_sql, (
                    department.department,
                    department.code,
                    school_id
                ))
            self.connection.commit()
        except Exception as e:
            print(f"An error occurred while inserting the department: {e}")
            self.connection.rollback()
