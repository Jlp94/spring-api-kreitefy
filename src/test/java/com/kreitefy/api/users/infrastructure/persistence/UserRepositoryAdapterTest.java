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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User(
                "user123", "Juan", "Perez", "password123", "juan@test.com", RolType.USUARIO
        );

        userEntity = new UserEntity();
        userEntity.setUsername("user123");
        userEntity.setNombre("Juan Antiguo");
        userEntity.setApellidos("Perez Antiguo");
        userEntity.setEmail("juan@test.com");
        userEntity.setPassword("password123");
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente")
    void shouldUpdateWhenUsuarioExists() {
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.entityToDomain(any(UserEntity.class))).thenReturn(user);

        User result = userRepositoryAdapter.save(user);

        assertNotNull(result);
        verify(userJpaRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Debe retornar un Optional con el usuario de dominio cuando el username existe")
    void shouldReturnOptionalWithUsuarioWhenUsernameExists() {
        when(userJpaRepository.findById("user123")).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDomain(userEntity)).thenReturn(user);

        Optional<User> result = userRepositoryAdapter.find("user123");

        assertTrue(result.isPresent(), "El Optional no debería estar vacío");
        assertEquals("user123", result.get().username());
        verify(userJpaRepository, times(1)).findById("user123");
        verify(userMapper, times(1)).entityToDomain(userEntity);
    }

    @Test
    @DisplayName("Debe retornar un Optional vacío cuando el username no existe en la base de datos")
    void shouldReturnEmptyOptionalWhenUsernameDoesNotExist() {
        when(userJpaRepository.findById("notfound")).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.find("notfound");

        assertTrue(result.isEmpty(), "El Optional debe estar vacío si el usuario no existe");
        verify(userJpaRepository, times(1)).findById("notfound");
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Debe retornar la lista completa de usuarios mapeados a objetos de dominio")
    void shouldReturnUsuarioListWhenUsersExist() {
        List<UserEntity> entities = List.of(userEntity);
        List<User> domainList = List.of(user);

        when(userJpaRepository.findAll()).thenReturn(entities);
        when(userMapper.toDomainListFromEntity(entities)).thenReturn(domainList);

        List<User> result = userRepositoryAdapter.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user123", result.get(0).username());
        verify(userJpaRepository, times(1)).findAll();

        verify(userMapper, times(1)).toDomainListFromEntity(entities);
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no existen usuarios registrados")
    void shouldReturnEmptyListWhenNoUsersExist() {
        List<UserEntity> emptyEntities = Collections.emptyList();
        List<User> emptyDomain = Collections.emptyList();

        when(userJpaRepository.findAll()).thenReturn(emptyEntities);
        when(userMapper.toDomainListFromEntity(emptyEntities)).thenReturn(emptyDomain);

        List<User> result = userRepositoryAdapter.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userJpaRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDomainListFromEntity(emptyEntities);
    }
    @Test
    @DisplayName("Debe retornar verdadero cuando el correo electrónico ya se encuentra registrado")
    void shouldReturnTrueWhenEmailAlreadyExists() {
        String emailTest = "juan@test.com";
        when(userJpaRepository.existsByEmail(emailTest)).thenReturn(true);

        boolean result = userRepositoryAdapter.existsByEmail(emailTest);
        assertTrue(result);
        verify(userJpaRepository, times(1)).existsByEmail(emailTest);
    }

    @Test
    @DisplayName("Debe retornar falso cuando el correo electrónico no está registrado")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        String emailTest = "nuevo@test.com";
        when(userJpaRepository.existsByEmail(emailTest)).thenReturn(false);

        boolean result = userRepositoryAdapter.existsByEmail(emailTest);
        assertFalse(result);
        verify(userJpaRepository, times(1)).existsByEmail(emailTest);
    }
}