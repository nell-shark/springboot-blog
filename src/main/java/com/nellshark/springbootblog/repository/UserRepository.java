package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NonNull
    @Query("SELECT user " +
            "FROM User user " +
            "WHERE user.id = :id")
    Optional<User> findById(@NonNull Long id);

    @Query("SELECT user " +
            "FROM User user " +
            "WHERE user.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT user " +
            "FROM User user " +
            "WHERE user.role = :role")
    List<User> findByRole(UserRole role);
}
