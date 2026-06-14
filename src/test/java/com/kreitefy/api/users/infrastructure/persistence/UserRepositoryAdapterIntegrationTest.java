package com.kreitefy.api.users.infrastructure.persistence;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.domain.type.RolType;
import com.kreitefy.api.users.infrastructure.mappers.UserMapper;
import com.kreitefy.api.users.infrastructure.persistence.adapter.UserRepositoryAdapter;

import com.kreitefy.api.users.infrastructure.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({UserRepositoryAdapter.class, UserRepositoryAdapterIntegrationTest.TestConfig.class})
class UserRepositoryAdapterIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }

    @Autowired
    private UserRepositoryAdapter userRepositoryAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Debe guardar un usuario físicamente en H2 y permitir recuperarlo con find")
    void shouldSaveAndFindUsuarioInH2() {
        User nuevoUsuario = new User(
                "test_integration", "Carlos", "Perez", "pass123", "carlos@test.com", RolType.USUARIO
        );
        userRepositoryAdapter.save(nuevoUsuario);

        Optional<User> resultado = userRepositoryAdapter.find("test_integration");

        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().nombre());
    }

}