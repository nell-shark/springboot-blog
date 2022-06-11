package com.nellshark.springbootblog.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    USER(Set.of(Permission.READ_ARTICLES)),

    ADMIN(Set.of(
            Permission.READ_ARTICLES,
            Permission.WRITE_ARTICLES,
            Permission.EDIT_ARTICLES,
            Permission.DELETE_ARTICLES));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}

