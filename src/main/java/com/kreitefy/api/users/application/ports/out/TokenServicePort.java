package com.kreitefy.api.users.application.ports.out;

import com.kreitefy.api.users.domain.models.User;

public interface TokenServicePort {
    String generateToken(User usuario);
    String generateRefreshToken(User usuario);
    String getUsernameFromToken(String token);
}
