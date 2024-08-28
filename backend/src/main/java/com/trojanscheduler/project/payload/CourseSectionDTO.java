package com.trojanscheduler.project.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseSectionDTO implements Comparable<CourseSectionDTO>{

    private int id;
    private int courseId;
    private String section;
    private String session;
    private String type;
    private Integer startTime;
    private Integer endTime;
    private Integer days;
    private String registered;
    private String instructor;
    private String location;

    @Override
    public int compareTo(CourseSectionDTO courseSectionDTO) {
        return this.section.compareTo(courseSectionDTO.getSection());
    }
}
