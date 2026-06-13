package com.kreitefy.api.users.infrastructure.rest.dtos.request;

import com.kreitefy.api.users.domain.type.RolType;

public record UserDto(
        String username,
        String nombre,
        String apellidos,
        String password,
        String email,
        RolType rol,
        Integer version
) { }
