package com.kreitefy.api.users.infrastructure.auth;

import com.kreitefy.api.users.application.ports.out.UserRepositoryPort;
import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.domain.type.RolType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        return processOAuth2User(clientRegistrationId, oAuth2User);
    }

    private OAuth2User processOAuth2User(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email;
        String username;
        String nombre;
        String apellidos;

        if ("google".equalsIgnoreCase(provider)) {
            email = (String) attributes.get("email");
            username = email;
            nombre = (String) attributes.get("given_name");
            apellidos = (String) attributes.get("family_name");
            if (nombre == null) {
                nombre = (String) attributes.get("name");
            }
            if (apellidos == null) {
                apellidos = ".";
            }
        } else if ("github".equalsIgnoreCase(provider)) {
            String login = (String) attributes.get("login");
            email = (String) attributes.get("email");
            if (email == null) {
                email = login + "@github.com";
            }
            username = login;

            String fullName = (String) attributes.get("name");
            if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.split(" ", 2);
                nombre = parts[0];
                apellidos = parts.length > 1 ? parts[1] : ".";
            } else {
                nombre = login;
                apellidos = ".";
            }
        } else {
            throw new OAuth2AuthenticationException("Provider not supported: " + provider);
        }

        Optional<User> existingUserOpt = userRepositoryPort.find(username);
        if (existingUserOpt.isEmpty()) {
            User newUser = new User(
                    username,
                    nombre,
                    apellidos,
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    email,
                    RolType.USUARIO
            );
            userRepositoryPort.save(newUser);
        }

        return oAuth2User;
    }
}
