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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.nellshark.springbootblog.model.UserRole.ROLE_ADMIN;
import static com.nellshark.springbootblog.model.UserRole.ROLE_MODERATOR;
import static com.nellshark.springbootblog.utils.FileUtils.APP_LOCATION;
import static com.nellshark.springbootblog.utils.FileUtils.STORAGE_FOLDER;
import static com.nellshark.springbootblog.utils.FileUtils.getNewFileName;
import static com.nellshark.springbootblog.utils.FileUtils.saveMultipartFile;

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
        log.info("Getting a user by id: " + id);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst().orElseThrow(() -> new UserNotFoundException("User with id='%s' wasn't found".formatted(id)));
    }

    public User getUserByEmail(String email) {
        log.info("Getting a user by email: " + email);
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElseThrow(() -> new UserNotFoundException("User with email='%s' wasn't found".formatted(email)));
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public List<User> getAllAdmins() {
        log.info("Getting all admins");
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals(ROLE_ADMIN)).toList();
    }

    public List<User> getAllModerators() {
        log.info("Getting all moderators");
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals(ROLE_MODERATOR)).toList();
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

        String newFileName = getNewFileName(file.getOriginalFilename());

        final String USERS_STORAGE_FOLDER = STORAGE_FOLDER + "/users";

        String filePath = APP_LOCATION
                + USERS_STORAGE_FOLDER + "/"
                + id + "/"
                + newFileName;

        saveMultipartFile(file, filePath);

        return USERS_STORAGE_FOLDER + "/" + id + "/" + newFileName;
    }

    public void deleteUserById(Long id) {
        log.info("Deleting the User by Id: " + id);
        userRepository.deleteById(id);
    }
}
