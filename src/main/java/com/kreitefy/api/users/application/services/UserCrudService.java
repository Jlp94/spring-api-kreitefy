package com.kreitefy.api.users.application.services;

import com.kreitefy.api.users.application.ports.out.UserRepositoryPort;
import com.kreitefy.api.shared.application.services.CrudService;
import com.kreitefy.api.shared.domain.errors.ConflictException;
import com.kreitefy.api.users.domain.models.User;

import com.kreitefy.api.users.domain.type.RolType;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserCrudService implements CrudService<User,String> {
    private final UserRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    UserCrudService(UserRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return usuarioRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(String username) {
        return usuarioRepositoryPort.find(username);
    }

    @Override
    @Transactional
    public User crear(User usuario) {
        if (usuarioRepositoryPort.find(usuario.username()).isPresent()) {
            throw new ConflictException("El usuario ya existe");
        }
        if (usuarioRepositoryPort.existsByEmail(usuario.email())) {
            throw new ConflictException("El email ya existe");
        }

        RolType rolFinal = (usuario.rol() == null) ? RolType.USUARIO : usuario.rol();
        User usuarioParaGuardar = new User(
                usuario.username(),
                usuario.nombre(),
                usuario.apellidos(),
                passwordEncoder.encode(usuario.password()),
                usuario.email(),
                rolFinal
        );
        return usuarioRepositoryPort.save(usuarioParaGuardar);
    }

    @Override
    @Transactional
    public User actualizar(User usuario) {
        User usuarioBD = usuarioRepositoryPort.find(usuario.username())
                .orElseThrow(() -> new ConflictException("Usuario no encontrado"));
        if (!usuarioBD.email().equalsIgnoreCase(usuario.email()) &&
                usuarioRepositoryPort.existsByEmail(usuario.email())) {
            throw new ConflictException("El email ya existe en el sistema");
        }
        String passwordFinal = comprobarPassword(usuario, usuarioBD);
        User usuarioActualizado = getUsuarioActualizado(usuario, usuarioBD, passwordFinal);
        return this.usuarioRepositoryPort.save(usuarioActualizado);
    }

    @NonNull
    private static User getUsuarioActualizado(User usuario, User usuarioBD, String passwordFinal) {
        return usuarioBD.actualizarPerfil(
                usuario.nombre(),
                usuario.apellidos(),
                usuario.email(),
                passwordFinal
        );
    }

    private String comprobarPassword(User usuario, User usuarioBD) {
        return (usuario.password() != null && !usuario.password().isBlank())
                ? passwordEncoder.encode(usuario.password())
                : usuarioBD.password();
    }

    @Override
    public void delete(String username) {

    }
}
