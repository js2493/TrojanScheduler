from repository.repository import PostgreRepository


class SectionRepository(PostgreRepository):
    insert_sql = """
    INSERT INTO sections (course_id, section, session, type, start_time, end_time, days, registered, instructor, location)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    ON CONFLICT(section, session)
    DO NOTHING
    RETURNING id;
    """

    def create_table(self):
        create_table_sql = """
            CREATE TABLE IF NOT EXISTS sections (
            id SERIAL PRIMARY KEY,
            section TEXT NOT NULL,
            session TEXT NOT NULL,
            type TEXT NOT NULL,
            start_time INTEGER NOT NULL,
            end_time INTEGER NOT NULL,
            days INTEGER NOT NULL,
            registered TEXT,
            instructor TEXT,
            location TEXT,
            course_id INTEGER NOT NULL, 
            FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
            UNIQUE(section, session)
        );
        """
        try:
            self.cursor.execute(create_table_sql)
            self.connection.commit()
        except Exception as e:
            print(f"An error occurred during table creation: {e}")
            self.connection.rollback()

    def insert(self, section, course_id):

        try:
            self.cursor.execute(self.insert_sql, (
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
        if course_ids:
            section_data = []
            for ind, section_list in enumerate(sections):
                for section in section_list:
                    section_data.append(
                        (course_ids[ind], section.section, section.session, section.type, section.start_time,
                         section.end_time, section.days, section.registered, section.instructor,
                         section.location))

            # section_data_2d = [[(course_ids[ind], section.section, section.session, section.type, section.start_time,
            #                   section.end_time, section.days, section.registered, section.instructor,
            #                   section.location) for section in section_list] for ind, section_list in enumerate(sections)]
            #
            # section_data = [data for sublist in section_data_2d for data in sublist]

            try:
                self.cursor.executemany(self.insert_sql, section_data)
                self.connection.commit()
            except Exception as e:
                print(f"An error occurred while inserting the section: {e}")
                self.connection.rollback()
