package com.trojanscheduler.project.service.interfaces;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;

import java.util.List;

public interface UserService {

    TrojanUser createUser(String username, String password);
    TrojanUser createUser(String username, String password, String email);
    TrojanUser createCalendar(String username, String calendarName);

    TrojanUser deleteCalendar(String username, Long calendarId);

    List<Calendar> getCalendars(String username);
    void deleteUser(String username);

}
