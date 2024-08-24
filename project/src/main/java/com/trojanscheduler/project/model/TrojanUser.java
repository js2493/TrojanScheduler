package com.trojanscheduler.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@Entity(name = "users")
@NoArgsConstructor
public class TrojanUser implements UserDetails {

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Calendar> calendars = new HashSet<>();

    @Id
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String email;

    public TrojanUser(String username, String password) {
        this(username, password, null);
    }

    public TrojanUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}
