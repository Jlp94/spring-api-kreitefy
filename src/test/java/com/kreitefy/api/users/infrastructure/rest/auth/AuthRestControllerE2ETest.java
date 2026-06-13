package com.kreitefy.api.users.infrastructure.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kreitefy.api.users.domain.type.RolType;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.UserDto;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.LoginRequestDto;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.SongDto;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.ArtistJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.jpa.StyleJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.jpa.AlbumJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthRestControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtistJpaRepository artistJpaRepository;

    @Autowired
    private StyleJpaRepository styleJpaRepository;

    @Autowired
    private AlbumJpaRepository albumJpaRepository;

    @Test
    @DisplayName("Debería registrar un usuario correctamente y luego iniciar sesión con éxito")
    void shouldRegisterAndLoginSuccessfully() throws Exception {
        UserDto normalUser = new UserDto(
                "e2e_normal_user",
                "E2E",
                "Normal",
                "Password123!",
                "e2e_normal@test.com",
                RolType.USUARIO,
                null
        );

        String registerResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(registerResponse).get("token").asText();

        LoginRequestDto loginRequest = new LoginRequestDto("e2e_normal_user", "Password123!");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        mockMvc.perform(get("/usuarios/historial")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Debería fallar el registro cuando el usuario ya existe (Violación de restricción única)")
    void shouldFailRegistrationWhenUserAlreadyExists() throws Exception {
        UserDto normalUser = new UserDto(
                "e2e_duplicate_user",
                "E2E",
                "Duplicate",
                "Password123!",
                "e2e_duplicate@test.com",
                RolType.USUARIO,
                null
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.message").value("El usuario ya existe"));
    }

    @Test
    @DisplayName("Debería fallar el inicio de sesión con contraseña incorrecta")
    void shouldFailLoginWithInvalidPassword() throws Exception {
        UserDto normalUser = new UserDto(
                "e2e_wrong_pass_user",
                "E2E",
                "WrongPass",
                "Password123!",
                "e2e_wrongpass@test.com",
                RolType.USUARIO,
                null
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isCreated());

        LoginRequestDto badLogin = new LoginRequestDto("e2e_wrong_pass_user", "WrongPassword!");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badLogin)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"));
    }

    @Test
    @DisplayName("Debería denegar acceso (403) a endpoint seguro si no se proporciona token")
    void shouldDenyAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/usuarios/historial"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debería denegar acceso (403) a endpoint seguro con token malformado")
    void shouldDenyAccessWithMalformedToken() throws Exception {
        mockMvc.perform(get("/usuarios/historial")
                        .header("Authorization", "Bearer token_completamente_invalido"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debería denegar acceso a endpoint de administración si el rol es USUARIO")
    void shouldDenyAdminEndpointForNormalUser() throws Exception {
        UserDto normalUser = new UserDto(
                "e2e_normal_auth_user",
                "E2E",
                "NormalAuth",
                "Password123!",
                "e2e_normalauth@test.com",
                RolType.USUARIO,
                null
        );

        String registerResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(normalUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(registerResponse).get("token").asText();

        SongDto songDto = new SongDto(
                null,
                "E2E Test Forbidden Song",
                180,
                0,
                9999L,
                9999L,
                LocalDateTime.now(),
                null
        );

        mockMvc.perform(post("/songs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debería permitir acceso a crear canción si el usuario tiene rol ADMIN")
    void shouldAllowAdminToCreateSong() throws Exception {
        // Guardar entidad de artista, estilo y álbum físicamente
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setNombre("E2E Test Artist");
        ArtistEntity savedArtist = artistJpaRepository.saveAndFlush(artistEntity);

        StyleEntity styleEntity = new StyleEntity();
        styleEntity.setEstilo("E2E Test Style");
        StyleEntity savedStyle = styleJpaRepository.saveAndFlush(styleEntity);

        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setNombre("E2E Test Album");
        albumEntity.setArtista(savedArtist);
        AlbumEntity savedAlbum = albumJpaRepository.saveAndFlush(albumEntity);

        // Registrar admin
        UserDto adminUser = new UserDto(
                "e2e_admin_auth_user",
                "E2E",
                "AdminAuth",
                "Password123!",
                "e2e_adminauth@test.com",
                RolType.ADMIN,
                null
        );

        String registerResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(registerResponse).get("token").asText();

        // Crear canción con ids correctos
        SongDto songDto = new SongDto(
                null,
                "E2E Test Allowed Song",
                210,
                0,
                savedAlbum.getId(),
                savedStyle.getId(),
                LocalDateTime.now(),
                null
        );

        mockMvc.perform(post("/songs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("E2E Test Allowed Song"));
    }
}
