package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.payload.CalendarDTO;
import com.trojanscheduler.project.service.interfaces.CalendarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<CalendarDTO> addSection(
            @RequestParam(name="calendar_id") Long calendar_id,
            @RequestParam(name="section_id") Long section_id) {

        CalendarDTO calendarDTO;
        if (calendar_id < 0) {
            calendarDTO = calendarService.anonymousEnrollSection(section_id);
        }
        else {
            calendarDTO = calendarService.enrollSection(calendar_id, section_id);
        }
        return new ResponseEntity<>(calendarDTO, HttpStatus.OK);
    }
    @DeleteMapping("/drop")
    public ResponseEntity<CalendarDTO> dropSection(
            @RequestParam(name="calendar_id") Long calendar_id,
            @RequestParam(name="section_id") Long section_id) {

        CalendarDTO calendarDTO = calendarService.dropSection(calendar_id, section_id);
        return new ResponseEntity<>(calendarDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<Section>> getSections(@RequestParam(name="calendar_id") Long calendar_id) {
        return new ResponseEntity<>(calendarService.getSections(calendar_id), HttpStatus.OK);
    }

}
