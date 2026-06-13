package com.kreitefy.api.users.infrastructure.auth;

import com.kreitefy.api.users.application.ports.out.UserRepositoryPort;
import com.kreitefy.api.users.domain.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepositoryPort userRepositoryPort;

    @Value("${app.oauth2.redirect-uri:http://localhost:4200/oauth2-redirect}")
    private String redirectUri;

    public OAuth2AuthenticationSuccessHandler(JwtService jwtService, UserRepositoryPort userRepositoryPort) {
        this.jwtService = jwtService;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String username;
        String email = oAuth2User.getAttribute("email");

        if (email != null) {
            username = email;
        } else {
            String login = oAuth2User.getAttribute("login");
            if (login != null) {
                username = login;
            } else {
                throw new UsernameNotFoundException("No se pudo identificar al usuario de OAuth2");
            }
        }

        User user = userRepositoryPort.find(username)
                .or(() -> {
                    String login = oAuth2User.getAttribute("login");
                    return login != null ? userRepositoryPort.find(login) : java.util.Optional.empty();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado tras autenticación OAuth2: " + username));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
