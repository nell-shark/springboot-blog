package com.nellshark.springbootblog.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    USER(Set.of(Authority.READ_ARTICLES)),

    MODERATOR(Set.of(Authority.READ_ARTICLES,
            Authority.CREATE_ARTICLES,
            Authority.EDIT_ARTICLES)),

    ADMIN(Set.of(
            Authority.READ_ARTICLES,
            Authority.CREATE_ARTICLES,
            Authority.EDIT_ARTICLES,
            Authority.DELETE_ARTICLES));

    private final Set<Authority> permissions;

    UserRole(Set<Authority> permissions) {
        this.permissions = permissions;
    }

    public Set<Authority> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}

