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
        log.info("get user by email: " + email);
        return appUserRepository.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email '%s' not found in db".formatted(email)));
    }

    public List<AppUser> getAllAdmins() {
        log.info("get all admins");
        return appUserRepository.findByRole(UserRole.ADMIN);
    }
}
