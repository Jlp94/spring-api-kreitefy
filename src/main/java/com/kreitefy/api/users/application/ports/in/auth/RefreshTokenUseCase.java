package com.kreitefy.api.users.application.ports.in.auth;

import com.kreitefy.api.users.domain.models.AuthToken;

public interface RefreshTokenUseCase {
    AuthToken refresh(String refreshToken);
    AuthToken refreshByUsername(String username);
}