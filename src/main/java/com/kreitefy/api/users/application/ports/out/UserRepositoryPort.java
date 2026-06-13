package com.kreitefy.api.users.application.ports.out;

import com.kreitefy.api.users.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User usuario);
    Optional<User> find(String username);
    List<User> findAll();
    boolean existsByEmail(String email);
}
