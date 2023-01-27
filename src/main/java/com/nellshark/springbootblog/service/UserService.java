package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.UserNotFoundException;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.nellshark.springbootblog.model.UserRole.ROLE_ADMIN;
import static com.nellshark.springbootblog.model.UserRole.ROLE_MODERATOR;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return getUserByEmail(email);
    }

    public User getUserById(Long id) {
        log.info("Getting the user by id: " + id);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with id='%s' wasn't found".formatted(id)));
    }

    public User getUserByEmail(String email) {
        log.info("Getting the user by email: " + email);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with email='%s' wasn't found".formatted(email)));
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public Set<User> getBosses() {
        log.info("Getting admins and moderators");
        return userRepository.findAll()
                .stream()
                .filter(user -> Set.of(ROLE_ADMIN, ROLE_MODERATOR).contains(user.getRole()))
                .collect(toSet());
    }

    public void save(User user) throws IOException {
        userRepository.save(user);
    }

    public void authenticateUserAndSetSession(User user, HttpServletRequest request) {
        request.getSession();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public void save(User user, MultipartFile avatar) throws IOException {
        log.info("Saving the user in the db: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (avatar != null && !avatar.isEmpty()) {
            String fileFolder = "/users/" + user.getId();
            String _avatar = fileService.saveMultipartFileToLocalStorage(avatar, fileFolder);
            user.setAvatar(_avatar);
        }
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        log.info("Deleting the User by Id: " + id);
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
