package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("/application-test.properties")
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testFindUserByEmail() {
        String email = "test@gmail.com";
        User user = new User(email, "password123");
        underTest.save(user);
        Optional<User> optionalUser = underTest.findByEmail(email);

        assertTrue(optionalUser.isPresent());
        assertEquals(email, optionalUser.get().getEmail());
    }

    @Test
    void testFindUserByRole() {
        User user = new User("test@gmail.com", "password123");
        underTest.save(user);
        List<User> users = underTest.findByRole(UserRole.USER);

        assertFalse(CollectionUtils.isEmpty(users));
        assertEquals(UserRole.USER, users.get(0).getRole());
    }
}
