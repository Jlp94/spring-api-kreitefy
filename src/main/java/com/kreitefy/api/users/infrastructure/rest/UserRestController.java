package com.kreitefy.api.users.infrastructure.rest;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.domain.models.AuthToken;
import com.kreitefy.api.users.infrastructure.mappers.UserMapper;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.UserDto;
import com.kreitefy.api.users.application.ports.in.auth.LoginCaseUse;
import com.kreitefy.api.users.application.ports.in.auth.RefreshTokenUseCase;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.UpdateUserRequestDto;
import com.kreitefy.api.users.infrastructure.rest.dtos.response.AuthResponseDto;

import com.kreitefy.api.shared.application.services.CrudService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserRestController {
    private final UserMapper usuarioMapper;
    private final CrudService<User,String> usuarioCrudService;
    private final LoginCaseUse loginService;
    private final RefreshTokenUseCase refreshService;

    public UserRestController(UserMapper usuarioMapper,
                              CrudService<User,String> usuarioCrudService,
                              LoginCaseUse loginService,
                              RefreshTokenUseCase refreshService) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioCrudService = usuarioCrudService;
        this.loginService = loginService;
        this.refreshService = refreshService;
    }

    @PostMapping(value ="/auth/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponseDto> register(@RequestBody UserDto usuarioDto) {
        usuarioCrudService.crear(usuarioMapper.dtoToDomain(usuarioDto));
        AuthToken token = loginService.login(usuarioDto.username(), usuarioDto.password());
        AuthResponseDto response = new AuthResponseDto(token.accessToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/users/me", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponseDto> actualizarPerfil(Principal principal, @RequestBody UpdateUserRequestDto usuarioRequest) {

        usuarioCrudService.actualizar(new User(
                principal.getName(),
                usuarioRequest.nombre(),
                usuarioRequest.apellidos(),
                usuarioRequest.password(),
                usuarioRequest.email(),
                null));

        AuthToken token;
        if (usuarioRequest.password() != null && !usuarioRequest.password().isEmpty()) {
            token = loginService.login(principal.getName(), usuarioRequest.password());
        } else {
            token = refreshService.refreshByUsername(principal.getName());
        }
        AuthResponseDto response = new AuthResponseDto(token.accessToken());

        return ResponseEntity.ok(response);
    }

}
