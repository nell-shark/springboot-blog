package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.UserNotFoundException;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.nellshark.springbootblog.model.UserRole.ROLE_ADMIN;
import static com.nellshark.springbootblog.model.UserRole.ROLE_MODERATOR;
import static com.nellshark.springbootblog.service.FileService.APP_LOCATION;
import static com.nellshark.springbootblog.service.FileService.STORAGE_FOLDER;
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
        log.info("Getting a user by id: " + id);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with id='%s' wasn't found".formatted(id)));
    }

    public User getUserByEmail(String email) {
        log.info("Getting a user by email: " + email);
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

    public Set<User> getAdminsAndModerators() {
        log.info("Getting admins and moderators");
        return userRepository.findAll()
                .stream()
                .filter(user -> Set.of(ROLE_ADMIN, ROLE_MODERATOR).contains(user.getRole()))
                .collect(toSet());
    }

    public User saveUser(User user) {
        log.info("Saving the user in the db: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        return userRepository.save(user);
    }

    public void updateUser(Long id, String email, String password, MultipartFile file) throws IOException {
        log.info("Updating the user with id: " + id);
        User user = getUserById(id);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (file != null && !file.isEmpty()) {
            String avatar = saveUserAvatar(file, id);
            user.setAvatar(avatar);
        }

        userRepository.save(user);
    }

    public String saveUserAvatar(MultipartFile file, Long id) throws IOException {
        log.info("Saving the User's Avatar: " + file.getOriginalFilename());

        String newFileName = fileService.getNewFileName(file.getOriginalFilename());

        final String USERS_STORAGE_FOLDER = STORAGE_FOLDER + "/users";

        String filePath = APP_LOCATION
                + USERS_STORAGE_FOLDER + "/"
                + id + "/"
                + newFileName;

        fileService.saveMultipartFile(file, filePath);

        return USERS_STORAGE_FOLDER + "/" + id + "/" + newFileName;
    }

    public void deleteUserById(Long id) {
        log.info("Deleting the User by Id: " + id);
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
