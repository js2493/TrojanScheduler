package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.TrojanUser;

public interface UserService {

    TrojanUser createUser(String username, String password);
    TrojanUser createUser(String username, String password, String email);
    TrojanUser createCalendar(String username, String calendarName);
}
