package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.payload.CalendarDTO;

import java.util.Set;

public interface CalendarService {

    CalendarDTO enrollSection(Long calendarId, Long sectionId);
    CalendarDTO dropSection(Long calendarId, Long sectionId);
    CalendarDTO anonymousEnrollSection(Long sectionId);
    Set<Section> getSections(Long calendarId);
}
