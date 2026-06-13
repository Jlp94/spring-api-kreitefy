package com.kreitefy.api.users.domain.models;

public record AuthToken(
        String accessToken,
        String refreshToken
) {
}
