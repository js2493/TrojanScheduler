package com.trojanscheduler.project.controller;

import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test() {
        return "Authentication successful!";
    }

    @PostMapping("/enroll")
    public ResponseEntity<TrojanUser> addSection(
            @RequestParam(name="user_id") Long user_id,
            @RequestParam(name="section_id") Long section_id) {

        TrojanUser user = userService.enrollSection(user_id, section_id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrojanUser> createUser(
            @RequestParam(name="username") String username,
            @RequestParam(name="password") String password) {

        TrojanUser user = userService.createUser(username, password);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
