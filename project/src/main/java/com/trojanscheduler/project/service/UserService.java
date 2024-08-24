package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.TrojanUser;

public interface UserService {

    TrojanUser enrollSection(String username, Long sectionId);
    TrojanUser dropSection(String username, Long sectionId);
    TrojanUser createUser(String username, String password);
    TrojanUser createUser(String username, String password, String email);
}
