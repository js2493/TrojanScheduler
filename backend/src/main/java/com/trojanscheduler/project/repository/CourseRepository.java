package com.trojanscheduler.project.repository;

import com.trojanscheduler.project.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        StringBuilder sqlBuilder = new StringBuilder("SELECT c.id as course_id, c.department, c.course_number, c.course_name, c.units, " +
                "c.description, c.term, s.* FROM sections s JOIN courses c ON s.course_id = c.id");
        StringBuilder whereBuilder = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!p.getTerm().isEmpty()) {
            params.add(p.getTerm());
            whereBuilder.append(" AND c.term = ?");
        }
        if (!p.getDepartment().isEmpty()) {
            params.add(p.getDepartment());
            whereBuilder.append(" AND c.department = ?");
        } else if (p.getSchoolId() > 0) {
            params.add(p.getSchoolId());
            sqlBuilder.append(" JOIN departments d ON c.department = d.code JOIN schools sc ON sc.id = d.school_id");
            whereBuilder.append(" AND sc.id = ?");
        }
        if (!p.getCourseNumber().isEmpty()) {
            params.add(p.getCourseNumber());
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
        } else if (p.isDenViterbi()) {
            whereBuilder.append(" AND s.location = 'DEN@Viterbi'");
        }
        if (p.getDays() > 0) {
            params.add(p.getDays());
            whereBuilder.append(" AND (s.days & ?) > 0");
        }

        sqlBuilder.append(whereBuilder);
        sqlBuilder.append(" ORDER BY s.section");

        Map<Integer, CourseDTO> courseMap = new HashMap<>();

        try (Stream<Map<String, Object>> stream = jdbcTemplate.queryForStream(
                sqlBuilder.toString(),
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("course_id", rs.getInt("course_id"));
                    row.put("department", rs.getString("department"));
                    row.put("course_number", rs.getString("course_number"));
                    row.put("course_name", rs.getString("course_name"));
                    row.put("units", rs.getString("units"));
                    row.put("description", rs.getString("description"));
                    row.put("term", rs.getString("term"));
                    row.put("id", rs.getInt("id"));
                    row.put("section", rs.getString("section"));
                    row.put("session", rs.getString("session"));
                    row.put("type", rs.getString("type"));
                    row.put("start_time", rs.getInt("start_time"));
                    row.put("end_time", rs.getInt("end_time"));
                    row.put("days", rs.getInt("days"));
                    row.put("registered", rs.getString("registered"));
                    row.put("instructor", rs.getString("instructor"));
                    row.put("location", rs.getString("location"));
                    return row;
                },
                params.toArray()
        )) {
            stream.forEach(row -> {
                int courseId = (Integer) row.get("course_id");

                CourseDTO courseDTO = courseMap.computeIfAbsent(courseId, id -> new CourseDTO(
                        id,
                        (String) row.get("department"),
                        (String) row.get("course_number"),
                        (String) row.get("course_name"),
                        (String) row.get("units"),
                        (String) row.get("description"),
                        (String) row.get("term"),
                        new ArrayList<>()
                ));

                CourseSectionDTO courseSectionDTO = new CourseSectionDTO(
                        (Integer) row.get("id"),
                        courseId,
                        (String) row.get("section"),
                        (String) row.get("session"),
                        (String) row.get("type"),
                        (Integer) row.get("start_time"),
                        (Integer) row.get("end_time"),
                        (Integer) row.get("days"),
                        (String) row.get("registered"),
                        (String) row.get("instructor"),
                        (String) row.get("location")
                );
                courseDTO.getSections().add(courseSectionDTO);
            });
        }

        return new ArrayList<>(courseMap.values());
    }

//    public List<CourseDTO> getCourses(CourseSearchParams p) {
//        StringBuilder sqlBuilder = new StringBuilder("SELECT c.department, c.course_number, c.course_name, c.units, " +
//                "c.description, c,term, s.* FROM sections s JOIN courses c ON s.course_id = c.id");
//        StringBuilder whereBuilder = new StringBuilder(" WHERE 1=1");
//        List<Object> params = new ArrayList<>();
//        if (!p.getTerm().isEmpty()) {
//            params.add(p.getTerm());
//            whereBuilder.append(" AND c.term = ?");
//        }
//        if (!p.getDepartment().isEmpty()) {
//            params.add(p.getDepartment());
//            whereBuilder.append(" AND c.department = ?");
//        }
//        else if (!p.getSchool().isEmpty()) {
//            params.add(p.getSchool());
//            sqlBuilder.append(" JOIN departments d ON c.department = d.code JOIN schools sc ON sc.id = d.school_id");
//            whereBuilder.append(" AND sc.school = ?");
//        }
//        if (!p.getCourseNumber().isEmpty()) {
//            params.add(p.getCourseNumber());
//            whereBuilder.append(" AND c.course_number = ?");
//        }
//        if (p.getStartTime() > 0) {
//            params.add(p.getStartTime());
//            whereBuilder.append(" AND s.start_time >= ?");
//        }
//        if (p.getEndTime() > 0) {
//            params.add(p.getEndTime());
//            whereBuilder.append(" AND s.end_time <= ?");
//        }
//        if (p.isOnline()) {
//            whereBuilder.append(" AND s.location = 'ONLINE'");
//        }
//        else if (p.isDenViterbi()) {
//            whereBuilder.append(" AND s.location = 'DEN@Viterbi'");
//        }
//        if (p.getDays() > 0) {
//            params.add(p.getDays());
//            whereBuilder.append(" AND (s.days & ?) > 0");
//        }
//
//        sqlBuilder.append(whereBuilder);
//        sqlBuilder.append(" ORDER BY s.section");
//
//        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
//        Map<Integer, CourseDTO> courseMap = new HashMap<>();
//
//        for (Map<String, Object> row : rows) {
//            int courseId = (Integer) row.get("course_id");
//
//            CourseDTO courseDTO = courseMap.get(courseId);
//            if (courseDTO == null) {
//                courseDTO = new CourseDTO(
//                        courseId,
//                        (String) row.get("department"),
//                        (String) row.get("course_number"),
//                        (String) row.get("course_name"),
//                        (String) row.get("units"),
//                        (String) row.get("description"),
//                        (String) row.get("term"),
//                        new ArrayList<>()
//                );
//                courseMap.put(courseId, courseDTO);
//            }
//
//            CourseSectionDTO courseSectionDTO = new CourseSectionDTO(
//                    (Integer) row.get("id"), courseId, (String) row.get("section"),
//                    (String) row.get("session"), (String) row.get("type"),
//                    (Integer) row.get("start_time"), (Integer) row.get("end_time"),
//                    (Integer) row.get("days"), (String) row.get("registered"),
//                    (String) row.get("instructor"), (String) row.get("location")
//            );
//            courseDTO.getSections().add(courseSectionDTO);
//        }
//
//        return new ArrayList<>(courseMap.values());
//    }





}

