import psycopg2
from dotenv import load_dotenv
import os

class PostgreRepository:

    def __init__(self):
        load_dotenv()
        self.connection = psycopg2.connect(
            dbname=os.environ.get('DB_NAME'),
            user=os.environ.get('DB_USER'),
            password=os.environ.get('DB_PASS'),
            host=os.environ.get('DB_HOST')
        )
        self.cursor = self.connection.cursor()
        self.create_table()

    def create_table(self):
        raise NotImplementedError("Subclasses should implement this method.")


    def close(self):
        try:
            if self.cursor:
                self.cursor.close()
            if self.connection:
                self.connection.close()
        except Exception as e:
            print(f"An error occurred while closing the connection: {e}")