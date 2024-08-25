package com.trojanscheduler.project.config.security;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.payload.UserSession;
import com.trojanscheduler.project.repository.CalendarRepository;
import com.trojanscheduler.project.repository.TrojanUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;

@Component
public class TrojanAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TrojanUserRepository userRepository;
    private final Calendar anonymousCalendar;
    private final UserSession userSession;
    private final CalendarRepository calendarRepository;

    @Autowired
    public TrojanAuthenticationSuccessHandler(TrojanUserRepository userRepository,
                                              Calendar anonymousCalendar,
                                              UserSession userSession,
                                              CalendarRepository calendarRepository) {
        this.userRepository = userRepository;
        this.anonymousCalendar = anonymousCalendar;
        this.userSession = userSession;
        this.calendarRepository = calendarRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        TrojanUser user = userRepository.findByUsername(username);
        if (!anonymousCalendar.getSections().isEmpty()) {
            Calendar calendar = new Calendar();
            calendar.setUser(user);
            calendar.setSections(new HashSet<>(anonymousCalendar.getSections()));
            user.getCalendars().add(calendar);
            calendarRepository.save(calendar);
            anonymousCalendar.getSections().clear();
            userSession.setCurrentCalendarId((long) calendar.getId());
        }
        userSession.setUsername(username);
        response.sendRedirect("/home");

    }
}
