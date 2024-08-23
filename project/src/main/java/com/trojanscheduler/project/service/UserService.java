package com.trojanscheduler.project.service;

import com.trojanscheduler.project.model.TrojanUser;

public interface UserService {

    TrojanUser enrollSection(Long userId, Long sectionId);
    TrojanUser dropSection(Long userId, Long sectionId);
    TrojanUser createUser(String username, String password);
}
