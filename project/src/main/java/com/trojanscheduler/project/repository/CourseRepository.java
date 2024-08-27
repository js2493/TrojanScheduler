package com.trojanscheduler.project.repository;

import com.trojanscheduler.project.payload.DepartmentDTO;
import com.trojanscheduler.project.payload.SchoolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CourseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SchoolDTO> getAllSchools() {
        String sqlQuery = "SELECT s.id, s.school FROM schools s";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery);

        return rows.stream()
                .map(row -> new SchoolDTO((Integer) row.get("id"), (String) row.get("school")))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getDepartmentsBySchool(String school) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT d.department, d.code, d.school_id" +
                " FROM departments d INNER JOIN courses c ON d.code = c.department");
        List<Object> params = new ArrayList<>();

        if (!school.isEmpty()) {
            sqlBuilder.append(" JOIN schools s ON s.id = d.school_id WHERE s.schools = ?");
            params.add(school);
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());

        List<DepartmentDTO> response = rows.stream()
                .map(data -> new DepartmentDTO((String) data.get("code"), (String) data.get("department"), (Integer) data.get("school_id")))
                .sorted()
                .collect(Collectors.toList());

        return response;
    }


}

