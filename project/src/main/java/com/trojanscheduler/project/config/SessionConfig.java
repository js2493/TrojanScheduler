package com.trojanscheduler.project.config;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.payload.UserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class SessionConfig {

//    Should be session scoped beans for actual application, singleton scoped for testing through postman

    @Bean
//    @SessionScope
    public Calendar anonymousCalendar() {
        Calendar calendar = new Calendar();
        calendar.setUser(null);
        calendar.setId(-1);

        return calendar;
    }

    @Bean
//    @SessionScope
    public UserSession userSession() {
        UserSession userSession = new UserSession();
        userSession.setUsername(null);
        userSession.setCurrentCalendarId((long) -1);
        return userSession;
    }

    
}
