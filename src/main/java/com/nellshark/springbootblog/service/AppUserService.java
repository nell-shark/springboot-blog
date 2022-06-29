package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.AppUser;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Find a user by email: " + email);
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email '%s' was not found in the db".formatted(email)));
    }

    public List<AppUser> findAllAdmins() {
        log.info("Find all admins");
        return appUserRepository.findByRole(UserRole.ADMIN);
    }

    public void save(AppUser appUser) {
        log.info("Save the user in the db: " + appUser);
        appUserRepository.save(appUser);
    }

    public List<AppUser> findAll() {
        log.info("Find all users");
        return appUserRepository.findAll();
    }
}
