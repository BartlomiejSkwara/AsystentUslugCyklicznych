package com.example.uslugicykliczne.security;

import com.example.uslugicykliczne.entity.AccountDataEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final AccountDataEntity accountDataEntity;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()->accountDataEntity.getRole());
    }

    @Override
    public String getPassword() {
        return accountDataEntity.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return accountDataEntity.getUsername();
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
