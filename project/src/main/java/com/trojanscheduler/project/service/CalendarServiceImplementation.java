package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.repository.CalendarRepository;
import com.trojanscheduler.project.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
public class CalendarServiceImplementation implements CalendarService{

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    @Transactional
    public Calendar enrollSection(Long calendarId, Long sectionId) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found"));

        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = calendar.getUser().getUsername();
        if (!currentName.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be logged in!");
        }

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        calendar.getSections().add(section);
        section.getCalendars().add(calendar);

        calendarRepository.save(calendar);
//        sectionRepository.save(section);
        return calendar;
    }

    @Override
    @Transactional
    public Calendar dropSection(Long calendarId, Long sectionId) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found"));

        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = calendar.getUser().getUsername();
        if (!currentName.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be logged in!");
        }

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        calendar.getSections().remove(section);
        section.getCalendars().remove(calendar);

        calendarRepository.save(calendar);
//        sectionRepository.save(section);
        return calendar;
    }
}
