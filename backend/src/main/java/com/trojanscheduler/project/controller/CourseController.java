package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.payload.CourseDTO;
import com.trojanscheduler.project.payload.CourseSearchParams;
import com.trojanscheduler.project.payload.DepartmentDTO;
import com.trojanscheduler.project.payload.SchoolDTO;
import com.trojanscheduler.project.service.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/api/schools")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        return new ResponseEntity<>(courseService.getAllSchools(), HttpStatus.OK);
    }

    @GetMapping("/api/departments")
    public ResponseEntity<List<DepartmentDTO>> getDepartments(
            @RequestParam(name="school", defaultValue = "", required = false) String school ) {
        return new ResponseEntity<>(courseService.getDepartmentsBySchool(school), HttpStatus.OK);
    }

    @GetMapping("/api/courses")
    public ResponseEntity<List<CourseDTO>> getCourses(
            @RequestParam(name="term", defaultValue = "") String term,
            @RequestParam(name="school", defaultValue = "", required = false) String school,
            @RequestParam(name="department", defaultValue = "", required = false) String department,
            @RequestParam(name="course_code", defaultValue = "", required = false) String courseCode,
            @RequestParam(name="course_number", defaultValue = "", required = false) String courseNumber,
            @RequestParam(name="start_time", defaultValue = "-1", required = false) String startTime,
            @RequestParam(name="end_time", defaultValue = "-1", required = false) String end_time,
            @RequestParam(name="days", defaultValue = "127", required = false) String days,
            @RequestParam(name="is_online", defaultValue = "false", required = false) String isOnline,
            @RequestParam(name="is_den_viterbi", defaultValue = "false", required = false) String isDenViterbi
            ) {

        boolean online = isOnline.equals("true");
        boolean denViterbi = isDenViterbi.equals("true");

        if (Objects.equals(days, "0")) days = "127";

        CourseSearchParams courseSearchParams = new CourseSearchParams(
                term, school, department, courseCode, courseNumber,
                Integer.parseInt(startTime), Integer.parseInt(end_time), Integer.parseInt(days),
                online, denViterbi
        );
        return new ResponseEntity<>(courseService.getCourses(courseSearchParams), HttpStatus.OK);
    }


}
