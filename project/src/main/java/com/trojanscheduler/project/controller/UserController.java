package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.payload.RegistrationDTO;
import com.trojanscheduler.project.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String logIn() {
        return "Logged in";
    }

    @PostMapping("/register")
    public ResponseEntity<TrojanUser> registerUser(@RequestBody RegistrationDTO userInfo) {
        TrojanUser user = userService.createUser(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create_calendar")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<TrojanUser> createCalendar(
            @RequestParam(name="username") String username,
            @RequestParam(name="calendar_name") String calendarName) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name + " username: " + username);

        TrojanUser user = userService.createCalendar(username, calendarName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
