from repository.repository import PostgreRepository


class SchoolRepository(PostgreRepository):
    insert_sql = """
    INSERT INTO schools (school)
    VALUES (%s)
    ON CONFLICT (school)
    DO NOTHING
    RETURNING id;
    """

    def create_table(self):
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS schools (
            id SERIAL PRIMARY KEY,
            school TEXT NOT NULL,
            UNIQUE (school)
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()

        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def insert(self, school):
        try:
            self.cursor.execute(self.insert_sql, (
                school,
            ))
            result = self.cursor.fetchone()
            self.connection.commit()
            if result:
                return result[0]
            else:
                return self.get_existing_id(school)

        except Exception as e:
            print(f"An error occurred while inserting the school: {e}")
            self.connection.rollback()

    def get_existing_id(self, school):
        self.cursor.execute("""
                       SELECT id FROM schools
                       WHERE school = %s 
                   """, (school,))
        result = self.cursor.fetchone()
        if result:
            return result[0]
        else:
            return -1