package com.nellshark.springbootblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "profile_photo")
    private byte[] profilePhotoBytes;

    @Transient
    public String profilePhoto() {
        if (profilePhotoBytes == null || profilePhotoBytes.length == 0) {
            return null;
        }
        return Base64Utils.encodeToString(profilePhotoBytes);
    }

    public AppUser(String email, String password, UserRole role) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AppUser(String email, String password, UserRole role, byte[] profilePhotoBytes) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.role = role;
        this.profilePhotoBytes = profilePhotoBytes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", profilePhotoBytes=" + (profilePhotoBytes != null) +
                '}';
    }
}
