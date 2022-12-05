package com.nellshark.springbootblog.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.nellshark.springbootblog.model.UserAuthority.*;

public enum UserRole {
    ROLE_USER(Set.of(WRITE_COMMENTS)),

    ROLE_ADMIN(Set.of(WRITE_COMMENTS, CREATE_ARTICLES, EDIT_ARTICLES, DELETE_ARTICLES));

    private final Set<UserAuthority> permissions;

    UserRole(Set<UserAuthority> permissions) {
        this.permissions = permissions;
    }

    public Set<UserAuthority> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}

