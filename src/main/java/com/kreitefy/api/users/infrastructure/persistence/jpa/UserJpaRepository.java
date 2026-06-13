package com.kreitefy.api.users.infrastructure.persistence.jpa;

import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
}
