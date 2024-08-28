package com.trojanscheduler.project.service;

import com.trojanscheduler.project.payload.CourseDTO;
import com.trojanscheduler.project.payload.CourseSearchParams;
import com.trojanscheduler.project.payload.DepartmentDTO;
import com.trojanscheduler.project.payload.SchoolDTO;
import com.trojanscheduler.project.repository.CourseRepository;
import com.trojanscheduler.project.service.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;


import java.util.List;

@Service
public class CourseServiceImplementation implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<SchoolDTO> getAllSchools() {
        return courseRepository.getAllSchools();
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return courseRepository.getDepartmentsBySchool(null);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsBySchool(String school) {
        return courseRepository.getDepartmentsBySchool(school);
    }
    @Override
    public List<CourseDTO> getCourses(CourseSearchParams courseSearchParams) {
        return courseRepository.getCourses(courseSearchParams);
    }
}
