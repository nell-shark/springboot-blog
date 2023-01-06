package com.nellshark.springbootblog.model;

import org.apache.commons.collections4.SetUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static com.nellshark.springbootblog.model.UserAuthority.CREATE_ARTICLES;
import static com.nellshark.springbootblog.model.UserAuthority.DELETE_ARTICLES;
import static com.nellshark.springbootblog.model.UserAuthority.EDIT_ARTICLES;
import static com.nellshark.springbootblog.model.UserAuthority.WRITE_COMMENTS;
import static java.util.stream.Collectors.toSet;

public enum UserRole {
    ROLE_USER(Set.of(WRITE_COMMENTS)),

    ROLE_MODERATOR(Set.of(WRITE_COMMENTS, EDIT_ARTICLES)),

    ROLE_ADMIN(Set.of(WRITE_COMMENTS, CREATE_ARTICLES, EDIT_ARTICLES, DELETE_ARTICLES));

    private final Set<UserAuthority> permissions;

    UserRole(Set<UserAuthority> permissions) {
        this.permissions = permissions;
    }

    public Set<UserAuthority> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return SetUtils.union(getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(toSet()), Set.of(new SimpleGrantedAuthority(this.name())));
    }
}
