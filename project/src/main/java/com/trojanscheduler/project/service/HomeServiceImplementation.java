package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImplementation implements HomeService {

    @Autowired
    private Calendar anonymousCalendar;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Override
    public List<Calendar> home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of(anonymousCalendar);
        }
        String username = authentication.getName();
        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        return user.getCalendars();
    }
}
