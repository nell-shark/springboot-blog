package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private FileService fileService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, fileService);
    }

    @Test
    void should_returnUser_when_getUserById() {
        User user = User.builder().id(1L).email("test@gmail.com").password("password").build();
        when(userRepository.findAll()).thenReturn(List.of(user));

        User result = userService.getUserById(user.getId());

        assertEquals(user, result);
    }

    @Test
    void should_returnUser_when_getUserEmail() {
        User user = User.builder().email("test@gmail.com").password("password").build();
        when(userRepository.findAll()).thenReturn(List.of(user));

        User result = userService.getUserByEmail(user.getEmail());

        assertEquals(user, result);
    }

    @Test
    void should_returnListOfUsers_when_getAllUsers() {
        User user = User.builder().email("test@gmail.com").password("password").build();
        List<User> userList = List.of(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertEquals(userList, result);
    }

    @Test
    void should_returnListOfUsers_when_getBosses() {
        User admin = User.builder().role(UserRole.ROLE_ADMIN).email("admin@gmail.com").password("password").build();
        User moderator = User.builder().role(UserRole.ROLE_MODERATOR).email("moderator@gmail.com").password("password").build();
        User user = User.builder().role(UserRole.ROLE_USER).email("user@gmail.com").password("password").build();
        List<User> userList = List.of(admin, moderator, user);
        when(userRepository.findAll()).thenReturn(userList);

        Set<User> result = userService.getBosses();

        assertEquals(Set.of(admin, moderator), result);
    }

    @Test
    void should_saveUser() throws Exception {
        User user = User.builder().email("test@gmail.com").password("password").build();
        when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());

        userService.save(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    void should_deleteUser_whenDeleteUserById() {
        User user = User.builder().id(1L).email("test@gmail.com").password("password").build();
        when(userRepository.findAll()).thenReturn(List.of(user));

        userService.deleteUserById(user.getId());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(userArgumentCaptor.capture());

        assertEquals(user, userArgumentCaptor.getValue());
    }
}