package com.kreitefy.api.users.infrastructure.rest.dtos.request;

public record LoginRequestDto(
        String username,
        String password
) { }