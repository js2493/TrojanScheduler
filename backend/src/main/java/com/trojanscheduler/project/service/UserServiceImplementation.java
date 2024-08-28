package com.trojanscheduler.project.service;

import com.trojanscheduler.project.exceptions.APIException;
import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.payload.LoginDTO;
import com.trojanscheduler.project.payload.UserSession;
import com.trojanscheduler.project.repository.CalendarRepository;
import com.trojanscheduler.project.repository.TrojanUserRepository;
import com.trojanscheduler.project.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private TrojanUserRepository userRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserSession userSession;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public TrojanUser createUser(String username, String password) {
        return this.createUser(username, password, null);
    }

    @Override
    @Transactional
    public TrojanUser createUser(String username, String password, String email) {
        Optional<TrojanUser> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (existingUser.isPresent()) {
            throw new APIException(String.format("User with the username %s already exists", username));
        }
        TrojanUser user = new TrojanUser(username, password, email);
        userDetailsManager.createUser(user);
        return user;
    }

    @Override
    @Transactional
    public TrojanUser createCalendar(String username, String calendarName) {
        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        Calendar calendar = new Calendar();
        calendar.setUser(user);
        calendar.setName(calendarName);

        user.getCalendars().add(calendar);

        calendarRepository.save(calendar);

        userSession.setCurrentCalendarId((long) calendar.getId());


        return user;
    }

    @Override
    public TrojanUser deleteCalendar(String username, Long calendarId) {

        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found"));

        user.getCalendars().remove(calendar);
        calendarRepository.delete(calendar);
        userDetailsManager.updateUser(user);

        return user;
    }

    @Override
    public List<Calendar> getCalendars(String username) {
        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        return user.getCalendars();
    }

    @Override
    public void deleteUser(String username) {
        userDetailsManager.deleteUser(username);
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok().body("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


}
