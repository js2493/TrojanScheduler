package com.trojanscheduler.project.service.interfaces;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.payload.CourseDTO;
import com.trojanscheduler.project.payload.CourseSearchParams;
import com.trojanscheduler.project.payload.DepartmentDTO;
import com.trojanscheduler.project.payload.SchoolDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CourseService {

    List<SchoolDTO> getAllSchools();
    List<DepartmentDTO> getAllDepartments();
    List<DepartmentDTO> getDepartmentsBySchool(String school);

    List<CourseDTO> getCourses(CourseSearchParams courseSearchParams);




}
