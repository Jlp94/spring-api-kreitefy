package com.kreitefy.api.users.infrastructure.persistence.adapter;

import com.kreitefy.api.users.application.ports.out.UserRepositoryPort;
import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.infrastructure.mappers.UserMapper;
import com.kreitefy.api.users.infrastructure.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository usuarioJpaRepository;
    private final UserMapper usuarioMapper;

    public UserRepositoryAdapter(UserJpaRepository usuarioJpaRepository, UserMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public User save(User usuario) {
        return usuarioMapper.entityToDomain(
                usuarioJpaRepository.save(
                        usuarioMapper.domainToEntity(usuario)
                ));
    }

    @Override
    public Optional<User> find(String username) {
        return usuarioJpaRepository.findById(username)
                .map(usuarioMapper::entityToDomain);
    }

    @Override
    public List<User> findAll() {
        return usuarioMapper.toDomainListFromEntity(usuarioJpaRepository.findAll());
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }
}