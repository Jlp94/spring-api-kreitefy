package com.kreitefy.api.users.infrastructure.rest.auth;

import com.kreitefy.api.users.domain.models.AuthToken;
import com.kreitefy.api.users.application.ports.in.auth.LoginCaseUse;
import com.kreitefy.api.users.application.ports.in.auth.RefreshTokenUseCase;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.LoginRequestDto;
import com.kreitefy.api.users.infrastructure.rest.dtos.response.AuthResponseDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {
    private final LoginCaseUse authService;
    private final RefreshTokenUseCase refreshService;

    public AuthRestController(LoginCaseUse authService,
                            RefreshTokenUseCase refreshService) {
        this.authService = authService;
        this.refreshService = refreshService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginDto, HttpServletResponse response) {
        AuthToken token = authService.login(loginDto.username(), loginDto.password());
        AuthResponseDto authResponse = new AuthResponseDto(token.accessToken());

        ResponseCookie cookie = ResponseCookie.from("refresh_token", token.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) {
        String tokenDeCookie = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    tokenDeCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (tokenDeCookie == null || tokenDeCookie.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            AuthToken token = refreshService.refresh(tokenDeCookie);
            AuthResponseDto authResponse = new AuthResponseDto(token.accessToken());

            ResponseCookie cookie = ResponseCookie.from("refresh_token", token.refreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}