package com.kreitefy.api.users.application.services.auth;

import com.kreitefy.api.users.application.ports.in.auth.RefreshTokenUseCase;
import com.kreitefy.api.users.application.ports.in.auth.LoginCaseUse;
import com.kreitefy.api.users.application.ports.out.UserRepositoryPort;
import com.kreitefy.api.users.application.ports.out.TokenServicePort;
import com.kreitefy.api.users.domain.models.AuthToken;
import com.kreitefy.api.shared.domain.errors.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements LoginCaseUse, RefreshTokenUseCase {
    private final UserRepositoryPort usuarioRepositoryPort;
    private final TokenServicePort tokenServicePort;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort usuarioRepositoryPort, TokenServicePort tokenServicePort,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.tokenServicePort = tokenServicePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthToken login(String username, String password) {
        return usuarioRepositoryPort.find(username)
                .filter(user -> passwordEncoder.matches(password, user.password()))
                .map(user -> {
                    String accessToken = tokenServicePort.generateToken(user);
                    String refreshToken = tokenServicePort.generateRefreshToken(user);
                    return new AuthToken(accessToken, refreshToken);
                })
                .orElseThrow(() -> new UnauthorizedException("Credenciales incorrectas."));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthToken refresh(String refreshToken) {
        try {
            String username = tokenServicePort.getUsernameFromToken(refreshToken);
            if (username == null || username.isBlank()) {
                throw new UnauthorizedException("Token de refresco no válido.");
            }
            return usuarioRepositoryPort.find(username)
                    .map(user -> {
                        String accessToken = tokenServicePort.generateToken(user);
                        String newRefreshToken = tokenServicePort.generateRefreshToken(user);
                        return new AuthToken(accessToken, newRefreshToken);
                    })
                    .orElseThrow(() -> new UnauthorizedException("Usuario no válido para refresco."));
        } catch (Exception e) {
            throw new UnauthorizedException("Token de refresco corrupto o expirado.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthToken refreshByUsername(String username) {
        return usuarioRepositoryPort.find(username)
                .map(user -> {
                    String accessToken = tokenServicePort.generateToken(user);
                    String refreshToken = tokenServicePort.generateRefreshToken(user);
                    return new AuthToken(accessToken, refreshToken);
                })
                .orElseThrow(() -> new UnauthorizedException("Usuario no válido para regenerar token."));
    }
}
