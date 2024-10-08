package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.model.Calendar;
import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.payload.LoginDTO;
import com.trojanscheduler.project.payload.RegistrationDTO;
import com.trojanscheduler.project.service.interfaces.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TrojanUser> registerUser(@RequestBody RegistrationDTO userInfo) {
        TrojanUser user = userService.createUser(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<String> deleteUser(@RequestParam(name="username") String username) {
        userService.deleteUser(username);
        return new ResponseEntity<>("Successfully deleted user: " + username, HttpStatus.OK);
    }

    @PostMapping("/calendar")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<TrojanUser> createCalendar(
            @RequestParam(name="username") String username,
            @RequestParam(name="calendar_name", required = false) String calendarName) {

        TrojanUser user = userService.createCalendar(username, calendarName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/calendar")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<TrojanUser> deleteCalendar(
            @RequestParam(name="username") String username,
            @RequestParam(name="calendar_id") Long calendarId) {

        TrojanUser user = userService.deleteCalendar(username, calendarId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<List<Calendar>> getCalendars(@RequestParam(name="username") String username) {
        return new ResponseEntity(userService.getCalendars(username), HttpStatus.OK);
    }




}
