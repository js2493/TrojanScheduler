package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@AllArgsConstructor
public class CourseSearchParams {
    private String term;
    private String school;
    private String department;
    private String courseCode;
    private String courseNumber;
    private int startTime;
    private int endTime;
    private int days;
    private boolean isOnline;
    private boolean isDenViterbi;
}

