package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.AppUser;
import com.nellshark.springbootblog.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource("/application-test.properties")
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testFindUserByEmail() {
        String email = "test@gmail.com";
        AppUser user = new AppUser(email, "password", UserRole.USER);
        underTest.save(user);

        boolean userExists = underTest.findByEmail(email).isPresent();

        assertTrue(userExists);
    }

    @Test
    void testFindUserByRole() {
        AppUser user = new AppUser("test@gmail.com", "password123", UserRole.USER);
        underTest.save(user);

        boolean hasUser = !underTest.findByRole(UserRole.USER).isEmpty();

        assertTrue(hasUser);
    }
}
