package com.kreitefy.api.users.infrastructure.rest.dtos.response;

import com.kreitefy.api.users.domain.type.RolType;

public record UserResponseDto(
        String userName,
        String nombre,
        String apellidos,
        String email,
        RolType rol
) { }