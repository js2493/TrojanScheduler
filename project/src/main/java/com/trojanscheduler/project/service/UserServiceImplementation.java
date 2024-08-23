package com.trojanscheduler.project.service;

import com.trojanscheduler.project.exceptions.APIException;
import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.repository.SectionRepository;
import com.trojanscheduler.project.repository.TrojanUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private TrojanUserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public TrojanUser enrollSection(Long userId, Long sectionId) {

        TrojanUser user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User does not exist"));
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        user.getSectionList().add(section);
        userRepository.save(user);

        section.getUsers().add(user);
        sectionRepository.save(section);

        return user;
    }

    @Override
    public TrojanUser dropSection(Long userId, Long sectionId) {
        return null;
    }

    @Override
    public TrojanUser createUser(String username, String password) {
        Optional<TrojanUser> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (existingUser.isPresent()) {
            throw new APIException(String.format("User with the username %s already exists", username));
        }
        TrojanUser user = new TrojanUser(username, password);
        userRepository.save(user);
        return user;
    }


}
