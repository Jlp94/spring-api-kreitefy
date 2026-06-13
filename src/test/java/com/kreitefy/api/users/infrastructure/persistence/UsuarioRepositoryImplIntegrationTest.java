package com.kreitefy.api.users.infrastructure.persistence;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.domain.type.RolType;
import com.kreitefy.api.users.infrastructure.mappers.UserMapper;
import com.kreitefy.api.users.infrastructure.persistence.adapter.UserRepositoryAdapter;

import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({UserRepositoryAdapter.class, UsuarioRepositoryImplIntegrationTest.TestConfig.class})
class UsuarioRepositoryImplIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }

    @Autowired
    private UserRepositoryAdapter usuarioRepository;

    @Autowired
    private UserJpaRepository usuarioJpaRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Debe guardar un usuario físicamente en H2 y permitir recuperarlo con find")
    void shouldSaveAndFindUsuarioInH2() {
        User nuevoUsuario = new User(
                "test_integration", "Carlos", "Perez", "pass123", "carlos@test.com", RolType.USUARIO
        );
        usuarioRepository.save(nuevoUsuario);

        Optional<User> resultado = usuarioRepository.find("test_integration");

        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().nombre());
    }

    @Test
    @DisplayName("Debe lanzar ObjectOptimisticLockingFailureException auténtica cuando se viola la concurrencia en H2")
    void shouldThrowRealOptimisticLockingExceptionWhenVersionConflicts() {
        UserEntity entity = new UserEntity();
        entity.setUsername("usuario_concurrente");
        entity.setNombre("Original");
        entity.setApellidos("Original");
        entity.setEmail("concurrente@test.com");
        entity.setPassword("123456");
        entity.setRol(RolType.USUARIO);
        entity.setVersion(null);

        UserEntity entidadPersistida = usuarioJpaRepository.saveAndFlush(entity);
        testEntityManager.clear();

        User peticionA = new User("usuario_concurrente", "Cambiado Por A", "Perez", "123", "a@t.com", RolType.USUARIO);
        User peticionB = new User("usuario_concurrente", "Cambiado Por B", "Perez", "123", "b@t.com", RolType.USUARIO);

        usuarioRepository.save(peticionA);

        testEntityManager.flush();
        testEntityManager.clear();

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            usuarioRepository.save(peticionB);
            testEntityManager.flush();
        }, "La base de datos debió rechazar la versión obsoleta");
    }
}