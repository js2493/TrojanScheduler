package com.trojanscheduler.project.payload;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.Section;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CalendarDTO {

    private int id;
    private String name;
    private Set<Section> sections;

    public CalendarDTO(Calendar calendar) {
        this.id = calendar.getId();
        this.name = calendar.getName();
        this.sections = calendar.getSections();
    }

}
