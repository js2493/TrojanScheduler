package com.trojanscheduler.project.controller;

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


}
