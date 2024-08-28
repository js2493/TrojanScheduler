package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.payload.CalendarDTO;
import com.trojanscheduler.project.payload.UserSession;
import com.trojanscheduler.project.repository.CalendarRepository;
import com.trojanscheduler.project.repository.SectionRepository;
import com.trojanscheduler.project.service.interfaces.CalendarService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CalendarServiceImplementation implements CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserSession userSession;

    @Autowired
    private Calendar anonymousCalendar;

    @Override
    @Transactional
    public CalendarDTO enrollSection(Long calendarId, Long sectionId) {

        Calendar calendar = this.getCalendarAuthenticated(calendarId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        calendar.getSections().add(section);
        section.getCalendars().add(calendar);

        calendarRepository.save(calendar);

        return new CalendarDTO(calendar);
    }

    @Override
    @Transactional
    public CalendarDTO dropSection(Long calendarId, Long sectionId) {

        Calendar calendar = this.getCalendarAuthenticated(calendarId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        calendar.getSections().remove(section);
        section.getCalendars().remove(calendar);

        calendarRepository.save(calendar);
        return new CalendarDTO(calendar);
    }

    @Override
    public CalendarDTO anonymousEnrollSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException("Section does not exist"));
        anonymousCalendar.getSections().add(section);

        return new CalendarDTO(anonymousCalendar);
    }

    @Override
    public Set<Section> getSections(Long calendarId) {
        Calendar calendar = this.getCalendarAuthenticated(calendarId);
        return calendar.getSections();     }

    private Calendar getCalendarAuthenticated(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found"));
        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = calendar.getUser().getUsername();
        if (!currentName.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be logged in!");
        }

        userSession.setCurrentCalendarId(calendarId);

        return calendar;
    }

}
