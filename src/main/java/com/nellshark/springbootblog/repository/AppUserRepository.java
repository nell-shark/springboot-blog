package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.AppUser;
import com.nellshark.springbootblog.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    @Query("SELECT user " +
            "FROM AppUser user " +
            "WHERE user.email = ?1")
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT user " +
            "FROM AppUser user " +
            "WHERE user.role = ?1")
    List<AppUser> findByRole(UserRole role);
}
