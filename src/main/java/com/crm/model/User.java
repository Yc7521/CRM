package com.crm.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public interface User extends UserDetails {
    @Override
    String getUsername();

    @Override
    String getPassword();

    default String[] getRoles() {
        return new String[]{this.getClass().getSimpleName()};
    }

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String username = this.getUsername();
        if (username != null) {
            authorities.addAll(Arrays.stream(getRoles()).map(SimpleGrantedAuthority::new).toList());
        }
        return authorities;
    }

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }
}
