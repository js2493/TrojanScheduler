package com.trojanscheduler.project.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseTest implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        String testSql =
                        "SELECT c.department, c.course_number, c.course_name " +
                        "FROM courses c " +
                        "JOIN sections s ON c.id = s.course_id " +
                        "WHERE c.department = (?)";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(testSql,"CSCI");
        System.out.println(rows);
    }
}

