package com.kreitefy.api.users.infrastructure.auth;

import com.kreitefy.api.users.domain.type.RolType;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.UserDto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {

    private final String userName;
    private final String userPassword;
    private final RolType rol;

    public AuthUserDetails(UserDto usuarioDto) {
        this.userName = usuarioDto.username();
        this.userPassword = usuarioDto.password();
        this.rol = usuarioDto.rol();
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override public String getPassword() {
        return userPassword;
    }

    @Override public String getUsername() {
        return userName;
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return true;
    }
}
