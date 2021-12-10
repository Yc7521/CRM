package com.crm.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 用户接口 实现 UserDetails
 */
public interface User extends UserDetails {
    /**
     * 获取用户名
     *
     * @return 用户名
     */
    @Override
    String getUsername();

    /**
     * 获取密码
     *
     * @return 密码
     */
    @Override
    String getPassword();

    /**
     * 获取用户权限
     *
     * @return 用户权限
     */
    default String[] getRoles() {
        return new String[]{this.getClass().getSimpleName()};
    }

    /**
     * 获取用户权限
     *
     * @return 用户权限
     */
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
