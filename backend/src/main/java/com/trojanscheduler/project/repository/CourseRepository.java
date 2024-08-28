package com.trojanscheduler.project.repository;

import com.trojanscheduler.project.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
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
        StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT d.*" +
                " FROM departments d INNER JOIN courses c ON d.code = c.department");
        List<Object> params = new ArrayList<>();

        if (!school.isEmpty()) {
            sqlBuilder.append(" JOIN schools s ON s.id = d.school_id WHERE s.school = ?");
            params.add(school);
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());

        return rows.stream()
                .map(data -> new DepartmentDTO((String) data.get("code"), (String) data.get("department"), (Integer) data.get("school_id")))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCourses(CourseSearchParams p) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT s.* FROM sections s");
        StringBuilder whereBuilder = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        boolean joinedCourse = false;
        if (!p.getTerm().isEmpty()) {
            params.add(p.getTerm());
            sqlBuilder.append(" JOIN courses c ON s.course_id = c.id");
            joinedCourse = true;
            whereBuilder.append(" AND c.term = ?");
        }
        if (!p.getDepartment().isEmpty()) {
            params.add(p.getDepartment());
            if (!joinedCourse) {
                sqlBuilder.append(" JOIN courses c ON s.course_id = c.id");
                joinedCourse = true;
            }
            whereBuilder.append(" AND c.department = ?");
        }
        else if (!p.getSchool().isEmpty()) {
            params.add(p.getSchool());
            if (!joinedCourse) {
                sqlBuilder.append(" JOIN courses c ON s.course_id = c.id");
                joinedCourse = true;
            }
            sqlBuilder.append(" JOIN departments d ON c.department = d.code JOIN schools sc ON sc.id = d.school_id");
            whereBuilder.append(" AND sc.school = ?");
        }
        if (!p.getCourseNumber().isEmpty()) {
            params.add(p.getCourseNumber());
            if (!joinedCourse) {
                sqlBuilder.append(" JOIN courses c ON s.course_id = c.id");
                joinedCourse = true;
            }
            whereBuilder.append(" AND c.course_number = ?");
        }
        if (p.getStartTime() > 0) {
            params.add(p.getStartTime());
            whereBuilder.append(" AND s.start_time >= ?");
        }
        if (p.getEndTime() > 0) {
            params.add(p.getEndTime());
            whereBuilder.append(" AND s.end_time <= ?");
        }
        if (p.isOnline()) {
            whereBuilder.append(" AND s.location = 'ONLINE'");
        }
        else if (p.isDenViterbi()) {
            whereBuilder.append(" AND s.location = 'DEN@Viterbi'");
        }
        if (p.getDays() > 0) {
            params.add(p.getDays());
            whereBuilder.append(" AND (s.days & ?) > 0");
        }

        sqlBuilder.append(whereBuilder);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
        Map<Integer, List<CourseSectionDTO>> sectionMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            CourseSectionDTO courseSectionDTO = new CourseSectionDTO(
                    (Integer) row.get("id"), (Integer) row.get("course_id"), (String) row.get("section"),
                    (String) row.get("session"), (String) row.get("type"), (Integer) row.get("start_time"),
                    (Integer) row.get("end_time"), (Integer) row.get("days"), (String) row.get("registered"),
                    (String) row.get("instructor"), (String) row.get("location")
            );
            int key = (Integer) row.get("course_id");
            if (!sectionMap.containsKey(key)) {
                sectionMap.put(key, new ArrayList<>());
            }
            sectionMap.get(key).add(courseSectionDTO);
        }
        List<CourseDTO> result = new ArrayList<>();
        StringBuilder courseSqlBuilder = new StringBuilder("SELECT c.* FROM courses c WHERE c.id = ?");
        for (int key: sectionMap.keySet()) {
            Map<String, Object> course = jdbcTemplate.queryForList(courseSqlBuilder.toString(), key).get(0);
            Collections.sort(sectionMap.get(key));
            CourseDTO courseDTO = new CourseDTO(key, (String) course.get("department"),
                    (String) course.get("course_number"), (String) course.get("course_name"),
                    (String) course.get("units"), (String) course.get("description"), (String) course.get("term"),
                    sectionMap.get(key));
            result.add(courseDTO);
        }
        Collections.sort(result);
        return result;
    }


}

