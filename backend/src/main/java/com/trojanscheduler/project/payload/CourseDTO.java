package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CourseDTO implements Comparable<CourseDTO>{
    private int courseId;
    private String department;
    private String courseNumber;
    private String courseName;
    private String units;
    private String description;
    private String term;
    private List<CourseSectionDTO> sections;


    @Override
    public int compareTo(CourseDTO courseDTO) {
        return this.courseNumber.compareTo(courseDTO.getCourseNumber());
    }
}
