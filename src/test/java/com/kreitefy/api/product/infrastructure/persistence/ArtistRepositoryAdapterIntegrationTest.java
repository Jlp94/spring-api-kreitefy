package com.kreitefy.api.product.infrastructure.persistence;

import com.kreitefy.api.product.infrastructure.mappers.ArtistMapper;
import com.kreitefy.api.product.infrastructure.persistence.adapters.ArtistRepositoryAdapter;
import com.kreitefy.api.product.domain.models.Artist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({ ArtistRepositoryAdapter.class, ArtistRepositoryAdapterIntegrationTest.TestConfig.class })
class ArtistRepositoryAdapterIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ArtistMapper artistaMapper() {
            return Mappers.getMapper(ArtistMapper.class);
        }
    }

    @Autowired
    private ArtistRepositoryAdapter artistaRepository;

    @Test
    @DisplayName("Debe guardar un artista físicamente en H2 y permitir recuperarlo")
    void shouldSaveAndFindArtistaInH2() {
        // Given
        Artist nuevoArtista = new Artist(null, "Linkin Park", null);

        // When
        Artist artistaGuardado = artistaRepository.save(nuevoArtista);
        Optional<Artist> resultado = artistaRepository.findById(artistaGuardado.id());

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Linkin Park", resultado.get().nombre());
        assertEquals(0, resultado.get().version(), "Hibernate debió iniciar la versión en 0 automáticamente");
    }

}