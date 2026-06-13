package com.kreitefy.api.shared.infrastructure.rest.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        String malformedJson = "{ \"titulo\": \"Test Song\", \"duracion\": ";

        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El cuerpo de la petición no es un JSON válido o tiene un formato incorrecto."));
    }

    @Test
    @WithMockUser(authorities = "USUARIO")
    public void shouldReturnBadRequestWhenPathVariableTypeMismatch() throws Exception {
        mockMvc.perform(get("/songs/texto"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El parámetro 'id' debe ser de tipo Long."));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldReturnNotFoundWhenRelatedEntityDoesNotExist() throws Exception {
        String newSongJson = """
                {
                    "titulo": "Integration Test Song",
                    "duracion": 120,
                    "cantRepro": 0,
                    "idAlbum": 999999,
                    "idEstiloMusical": 999999
                }
                """;

        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSongJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("El registro relacionado no existe o ha sido eliminado."));
    }

    @Test
    public void shouldReturnForbiddenWhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/songs/1"))
                .andExpect(status().isForbidden());
    }
}
