package com.trojanscheduler.project.service;

import com.trojanscheduler.project.exceptions.APIException;
import com.trojanscheduler.project.model.Section;
import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.repository.SectionRepository;
import com.trojanscheduler.project.repository.TrojanUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private TrojanUserRepository userRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public TrojanUser enrollSection(String username, Long sectionId) {

        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        user.getSectionList().add(section);
        userDetailsManager.updateUser(user);

        section.getUsers().add(user);
        sectionRepository.save(section);

        return user;
    }

    @Override
    public TrojanUser dropSection(String username, Long sectionId) {

        TrojanUser user = (TrojanUser) userDetailsManager.loadUserByUsername(username);
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new NoSuchElementException("Section does not exist"));

        user.getSectionList().remove(section);
        userDetailsManager.updateUser(user);

        section.getUsers().remove(user);
        sectionRepository.save(section);

        return user;
    }

    @Override
    public TrojanUser createUser(String username, String password) {
        return this.createUser(username, password, null);
    }

    @Override
    public TrojanUser createUser(String username, String password, String email) {
        Optional<TrojanUser> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (existingUser.isPresent()) {
            throw new APIException(String.format("User with the username %s already exists", username));
        }
        TrojanUser user = new TrojanUser(username, password, email);
        userDetailsManager.createUser(user);
        return user;
    }


}
