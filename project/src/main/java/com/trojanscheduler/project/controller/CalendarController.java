package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.service.CalendarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<Calendar> addSection(
            @RequestParam(name="calendar_id") Long calendar_id,
            @RequestParam(name="section_id") Long section_id) {

        Calendar calendar = calendarService.enrollSection(calendar_id, section_id);
        return new ResponseEntity<>(calendar, HttpStatus.OK);
    }
    @DeleteMapping("/drop")
//    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<Calendar> dropSection(
            @RequestParam(name="calendar_id") Long calendar_id,
            @RequestParam(name="section_id") Long section_id) {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(name + " username: " + username);
        Calendar calendar = calendarService.dropSection(calendar_id, section_id);
        return new ResponseEntity<>(calendar, HttpStatus.OK);
    }

}
