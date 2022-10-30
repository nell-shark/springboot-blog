package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserByEmail(email);
    }

    public User getById(Long id) {
        log.info("Find a user by id: " + id);
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id='%s' wasn't found".formatted(id)));
    }

    public User getUserByEmail(String email) {
        log.info("Find a user by email: " + email);
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email '%s' was not found".formatted(email)));
    }

    public List<User> getAllUsers() {
        log.info("Find all users");
        return userRepository.findAll();
    }

    public List<User> getAllAdmins() {
        log.info("Find all admins");
        return userRepository.findByRole(UserRole.ADMIN);
    }

    public void saveUser(User user) {
        log.info("Save the user in the db: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
