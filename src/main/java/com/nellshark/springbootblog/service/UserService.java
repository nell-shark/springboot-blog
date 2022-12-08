package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.UserNotFoundException;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return getUserByEmail(email);
    }

    public User getUserById(Long id) {
        log.info("Find a user by id: " + id);
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id='%s' wasn't found".formatted(id)));
    }

    public User getUserByEmail(String email) {
        log.info("Find a user by email: " + email);
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email='%s' wasn't found".formatted(email)));
    }

    public List<User> getAllUsers() {
        log.info("Find all users");
        return userRepository.findAll();
    }

    public List<User> getAllAdmins() {
        log.info("Find all admins");
        return userRepository.findByRole(UserRole.ROLE_ADMIN);
    }

    public List<User> getAllModerators() {
        log.info("Find all moderators");
        return userRepository.findByRole(UserRole.ROLE_MODERATOR);
    }

    public User saveUser(User user) {
        log.info("Save the user in the db: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateEmail(String newEmail, User user) {
        log.info("Update the user's email: " + user);
        if (user.getEmail().equals(newEmail)) return;

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updatePassword(String newPassword, User user) {
        log.info("Update the user's password: " + user);
        String encodedPassword = passwordEncoder.encode(newPassword);
        if (user.getPassword().equals(encodedPassword)) return;

        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
