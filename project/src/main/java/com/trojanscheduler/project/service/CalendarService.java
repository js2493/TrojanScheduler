package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;

public interface CalendarService {

    Calendar enrollSection(Long calendarId, Long sectionId);
    Calendar dropSection(Long calendarId, Long sectionId);
}
