package com.kreitefy.api.users.infrastructure.persistence;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.domain.type.RolType;
import com.kreitefy.api.users.infrastructure.persistence.adapter.UserRepositoryAdapter;

import com.kreitefy.api.users.infrastructure.mappers.UserMapper;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import com.kreitefy.api.users.infrastructure.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryImplTest {

    @Mock
    private UserJpaRepository usuarioJpaRepository;

    @Mock
    private UserMapper usuarioMapper;

    @InjectMocks
    private UserRepositoryAdapter usuarioRepository;

    private User usuarioDomain;
    private UserEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioDomain = new User(
                "user123", "Juan", "Perez", "password123", "juan@test.com", RolType.USUARIO
        );

        usuarioEntity = new UserEntity();
        usuarioEntity.setUsername("user123");
        usuarioEntity.setNombre("Juan Antiguo");
        usuarioEntity.setApellidos("Perez Antiguo");
        usuarioEntity.setEmail("juan@test.com");
        usuarioEntity.setPassword("password123");
        usuarioEntity.setVersion(4);
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente y sincronizar su versión para el bloqueo optimista")
    void shouldUpdateAndSetVersionWhenUsuarioExists() {
        when(usuarioJpaRepository.findById("user123")).thenReturn(Optional.of(usuarioEntity));
        when(usuarioJpaRepository.save(any(UserEntity.class))).thenReturn(usuarioEntity);
        when(usuarioMapper.entityToDomain(any(UserEntity.class))).thenReturn(usuarioDomain);

        User result = usuarioRepository.save(usuarioDomain);

        assertNotNull(result);
        verify(usuarioJpaRepository, times(1)).findById("user123");
        assertEquals(5, usuarioEntity.getVersion(), "Se debió asignar la versión del DTO/Dominio a la Entidad para proteger contra concurrencia");
        assertEquals("juan@test.com", usuarioEntity.getEmail());
        verify(usuarioJpaRepository, times(1)).save(usuarioEntity);
    }

    @Test
    @DisplayName("Debe lanzar ObjectOptimisticLockingFailureException cuando la versión del dominio es menor que la de la base de datos")
    void shouldThrowOptimisticLockingExceptionWhenDomainVersionIsLessThanDatabaseVersion() {
        User usuarioVersionVieja = new User(
                "user123", "Juan", "Perez", "password123", "juan@test.com", RolType.USUARIO
        );

        UserEntity usuarioEntityBaseDatos = new UserEntity();
        usuarioEntityBaseDatos.setUsername("user123");
        usuarioEntityBaseDatos.setVersion(5);

        when(usuarioJpaRepository.findById("user123")).thenReturn(Optional.of(usuarioEntityBaseDatos));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            usuarioRepository.save(usuarioVersionVieja);
        }, "Se esperaba un fallo de bloqueo optimista porque el cliente envió datos desactualizados");

        verify(usuarioJpaRepository, times(1)).findById("user123");
        verify(usuarioJpaRepository, never()).save(any(UserEntity.class));
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    @DisplayName("Debe retornar un Optional con el usuario de dominio cuando el username existe")
    void shouldReturnOptionalWithUsuarioWhenUsernameExists() {
        when(usuarioJpaRepository.findById("user123")).thenReturn(Optional.of(usuarioEntity));
        when(usuarioMapper.entityToDomain(usuarioEntity)).thenReturn(usuarioDomain);

        Optional<User> result = usuarioRepository.find("user123");

        assertTrue(result.isPresent(), "El Optional no debería estar vacío");
        assertEquals("user123", result.get().username());
        verify(usuarioJpaRepository, times(1)).findById("user123");
        verify(usuarioMapper, times(1)).entityToDomain(usuarioEntity);
    }

    @Test
    @DisplayName("Debe retornar un Optional vacío cuando el username no existe en la base de datos")
    void shouldReturnEmptyOptionalWhenUsernameDoesNotExist() {
        when(usuarioJpaRepository.findById("notfound")).thenReturn(Optional.empty());

        Optional<User> result = usuarioRepository.find("notfound");

        assertTrue(result.isEmpty(), "El Optional debe estar vacío si el usuario no existe");
        verify(usuarioJpaRepository, times(1)).findById("notfound");
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    @DisplayName("Debe retornar la lista completa de usuarios mapeados a objetos de dominio")
    void shouldReturnUsuarioListWhenUsersExist() {
        List<UserEntity> entities = List.of(usuarioEntity);
        List<User> domainList = List.of(usuarioDomain);

        when(usuarioJpaRepository.findAll()).thenReturn(entities);
        when(usuarioMapper.toDomainListFromEntity(entities)).thenReturn(domainList);

        List<User> result = usuarioRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user123", result.get(0).username());
        verify(usuarioJpaRepository, times(1)).findAll();

        verify(usuarioMapper, times(1)).toDomainListFromEntity(entities);
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no existen usuarios registrados")
    void shouldReturnEmptyListWhenNoUsersExist() {
        List<UserEntity> emptyEntities = Collections.emptyList();
        List<User> emptyDomain = Collections.emptyList();

        when(usuarioJpaRepository.findAll()).thenReturn(emptyEntities);
        when(usuarioMapper.toDomainListFromEntity(emptyEntities)).thenReturn(emptyDomain);

        List<User> result = usuarioRepository.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioJpaRepository, times(1)).findAll();
        verify(usuarioMapper, times(1)).toDomainListFromEntity(emptyEntities);
    }
    @Test
    @DisplayName("Debe retornar verdadero cuando el correo electrónico ya se encuentra registrado")
    void shouldReturnTrueWhenEmailAlreadyExists() {
        String emailTest = "juan@test.com";
        when(usuarioJpaRepository.existsByEmail(emailTest)).thenReturn(true);

        boolean result = usuarioRepository.existsByEmail(emailTest);
        assertTrue(result);
        verify(usuarioJpaRepository, times(1)).existsByEmail(emailTest);
    }

    @Test
    @DisplayName("Debe retornar falso cuando el correo electrónico no está registrado")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        String emailTest = "nuevo@test.com";
        when(usuarioJpaRepository.existsByEmail(emailTest)).thenReturn(false);

        boolean result = usuarioRepository.existsByEmail(emailTest);
        assertFalse(result);
        verify(usuarioJpaRepository, times(1)).existsByEmail(emailTest);
    }
}