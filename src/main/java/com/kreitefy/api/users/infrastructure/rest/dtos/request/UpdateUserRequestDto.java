package com.kreitefy.api.users.infrastructure.rest.dtos.request;

public record UpdateUserRequestDto(
        String nombre,
        String apellidos,
        String email,
        String password
) { }
