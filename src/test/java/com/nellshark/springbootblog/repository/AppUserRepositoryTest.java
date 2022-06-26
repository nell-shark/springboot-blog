package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.AppUser;
import com.nellshark.springbootblog.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testFindByEmail() {
        String email = "test@gmail.com";
        AppUser appUser = new AppUser(email, "password123", UserRole.USER);
        underTest.save(appUser);

        boolean isUserExists = underTest.findByEmail(email).isPresent();

        assertTrue(isUserExists);
    }

    @Test
    void testFindByRole() {
        AppUser appUser = new AppUser("test@gmail.com", "password123", UserRole.USER);
        underTest.save(appUser);

        boolean hasUsers = !underTest.findByRole(UserRole.USER).isEmpty();
        boolean hasAdmins = !underTest.findByRole(UserRole.ADMIN).isEmpty();

        assertTrue(hasUsers);
        assertFalse(hasAdmins);
    }
}
