from repository.repository import PostgreRepository


class SectionRepository(PostgreRepository):

    def create_table(self):
        create_table_sql = """
            CREATE TABLE IF NOT EXISTS sections (
            id SERIAL PRIMARY KEY,
            section VARCHAR(20) NOT NULL,
            session VARCHAR(20) NOT NULL,
            type VARCHAR(50) NOT NULL,
            start_time INTEGER NOT NULL,
            end_time INTEGER NOT NULL,
            days INTEGER NOT NULL,
            registered VARCHAR(50),
            instructor VARCHAR(255),
            location VARCHAR(255),
            course_id INTEGER REFERENCES courses(id) ON DELETE CASCADE
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()
        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def insert(self, section, course_id):
        insert_section_sql = """
        INSERT INTO sections (course_id, section, session, type, start_time, end_time, days, registered, instructor, location)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING id;
        """
        try:
            self.cursor.execute(insert_section_sql, (
                course_id,
                section.section,
                section.session,
                section.type,
                section.start_time,
                section.end_time,
                section.days,
                section.registered,
                section.instructor,
                section.location
            ))
            section_id = self.cursor.fetchone()[0]
            self.connection.commit()
            return section_id
        except Exception as e:
            print(f"An error occurred while inserting the section: {e}")
            self.connection.rollback()
            return None

    def batch_insert(self, sections, course_ids):
        insert_section_sql = """
        INSERT INTO sections (course_id, section, session, type, start_time, end_time, days, registered, instructor, location)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING id;
        """
        try:
            for ind, section_list in enumerate(sections):
                for section in section_list:
                    self.cursor.execute(insert_section_sql, (
                        course_ids[ind],
                        section.section,
                        section.session,
                        section.type,
                        section.start_time,
                        section.end_time,
                        section.days,
                        section.registered,
                        section.instructor,
                        section.location
                    ))
            self.connection.commit()
        except Exception as e:
            print(f"An error occurred while inserting the course: {e}")
            self.connection.rollback()
