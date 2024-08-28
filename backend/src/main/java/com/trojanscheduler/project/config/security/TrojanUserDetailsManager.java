package com.trojanscheduler.project.config.security;

import com.trojanscheduler.project.model.TrojanUser;
import com.trojanscheduler.project.repository.TrojanUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrojanUserDetailsManager implements UserDetailsManager {

    private final TrojanUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TrojanUserDetailsManager(TrojanUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void createUser(UserDetails user) {
        TrojanUser trojanUser = (TrojanUser) user;
        trojanUser.setPassword(passwordEncoder.encode(trojanUser.getPassword()));
        userRepository.save(trojanUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((TrojanUser) user);
    }

    @Override
    public void deleteUser(String username) {
        Optional<TrojanUser> user = Optional.ofNullable(userRepository.findByUsername(username));
        user.ifPresent(userRepository::delete);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        TrojanUser user = Optional.ofNullable(userRepository.findByUsername(currentUsername))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        Optional<TrojanUser> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
