package com.kreitefy.api.product.infrastructure.persistence;

import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.infrastructure.mappers.AlbumMapper;
import com.kreitefy.api.product.infrastructure.persistence.adapters.AlbumRepositoryAdapter;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AlbumRepositoryAdapter.class, AlbumRepositoryAdapterIntegrationTest.TestConfig.class})
class AlbumRepositoryAdapterIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AlbumMapper albumMapper() {
            return Mappers.getMapper(AlbumMapper.class);
        }
    }

    @Autowired
    private AlbumRepositoryAdapter albumRepositoryPort;

    @Autowired
    private EntityManager entityManager;

    private ArtistEntity artistaPersistido;

    @BeforeEach
    void setUp() {
        artistaPersistido = new ArtistEntity();
        artistaPersistido.setNombre("Fito y Fitipaldis");
        artistaPersistido.setVersion(0);
        entityManager.persist(artistaPersistido);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe inicializar la versión a 0 al crear un nuevo álbum")
    void shouldInitializeVersionToZeroWhenCreate() {
        Artist artistaDominio = new Artist(artistaPersistido.getId(), "Fito y Fitipaldis", 0);
        Album nuevoAlbum = new Album(null, "Por la boca vive el pez", "imagen_base64", artistaDominio, null);

        Album guardado = albumRepositoryPort.save(nuevoAlbum);

        assertNotNull(guardado.id());
        assertEquals(0, guardado.version(), "Un álbum nuevo debe nacer con la versión 0");
    }

    @Test
    @DisplayName("Debe incrementar la versión al actualizar un álbum existente")
    void shouldIncrementVersionWhenUpdate() {
        // Given
        AlbumEntity entity = new AlbumEntity();
        entity.setNombre("Antes de mirar");
        entity.setArtista(artistaPersistido);
        entity.setVersion(0);
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();

        Artist artistaDominio = new Artist(artistaPersistido.getId(), "Fito y Fitipaldis", 0);
        Album albumParaActualizar = new Album(entity.getId(), "Antes de mirar (Modificado)", "nueva_img", artistaDominio, 0);

        // When
        albumRepositoryPort.save(albumParaActualizar);
        entityManager.flush();
        entityManager.clear();

        AlbumEntity entidadActualizada = entityManager.find(AlbumEntity.class, entity.getId());

        // Then
        assertNotNull(entidadActualizada);
        assertEquals(1, entidadActualizada.getVersion(), "Hibernate debe incrementar la versión a 1 en la tabla tras el update");
    }

    @Test
    @DisplayName("Debe lanzar ObjectOptimisticLockingFailureException si se intenta actualizar con una versión menor")
    void shouldThrowExceptionWhenVersionConflictOccurs() {
        // Given
        AlbumEntity entity = new AlbumEntity();
        entity.setNombre("Soldadito Marinero");
        entity.setArtista(artistaPersistido);
        entity.setVersion(2);
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();

        Artist artistaDominio = new Artist(artistaPersistido.getId(), "Fito y Fitipaldis", 0);
        Album albumDesfasado = new Album(entity.getId(), "Soldadito Marinero v2", "img", artistaDominio, 1);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            albumRepositoryPort.save(albumDesfasado);
            entityManager.flush();
        }, "Se esperaba un conflicto de bloqueo optimista debido a la versión obsoleta");
    }
}