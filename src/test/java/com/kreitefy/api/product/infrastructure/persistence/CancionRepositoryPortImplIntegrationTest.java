package com.kreitefy.api.product.infrastructure.persistence;

import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.product.infrastructure.mappers.SongMapper;
import com.kreitefy.api.product.infrastructure.persistence.adapters.SongRepositoryAdapter;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.AlbumJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.jpa.ArtistJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.jpa.SongJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.jpa.StyleJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        SongRepositoryAdapter.class,
        com.kreitefy.api.product.infrastructure.mappers.SongMapperImpl.class,
        com.kreitefy.api.product.infrastructure.mappers.AlbumMapperImpl.class,
        com.kreitefy.api.product.infrastructure.mappers.StyleMapperImpl.class
})
class CancionRepositoryPortImplIntegrationTest {

    @Autowired
    private SongRepositoryAdapter cancionRepository;

    @Autowired
    private SongJpaRepository cancionJpaRepository;

    @Autowired
    private AlbumJpaRepository albumJpaRepository;

    @Autowired
    private ArtistJpaRepository artistaJpaRepository;

    @Autowired
    private StyleJpaRepository estiloMusicalJpaRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Debe guardar una canción físicamente en H2 y permitir recuperarla")
    void shouldSaveAndFindCancionInH2() {
        ArtistEntity artista = new ArtistEntity();
        artista.setNombre("Metallica");
        ArtistEntity artistaGuardado = artistaJpaRepository.saveAndFlush(artista);

        AlbumEntity album = new AlbumEntity();
        album.setNombre("Master of Puppets");
        album.setArtista(artistaGuardado);
        AlbumEntity albumGuardado = albumJpaRepository.saveAndFlush(album);

        StyleEntity estilo = new StyleEntity();
        estilo.setEstilo("Rock");
        StyleEntity estiloGuardado = estiloMusicalJpaRepository.saveAndFlush(estilo);

        Album albumDomain = new Album(albumGuardado.getId(), albumGuardado.getNombre(), null, null, 0);
        Style estiloDomain = new Style(estiloGuardado.getId(), estiloGuardado.getEstilo());

        Song nuevaCancion = new Song(null, "Enter Sandman", 300, 0, albumDomain, estiloDomain, null, null);

        // When
        Song cancionGuardada = cancionRepository.save(nuevaCancion);
        Optional<Song> resultado = cancionRepository.findById(cancionGuardada.id());

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Enter Sandman", resultado.get().titulo());
        assertEquals(0, resultado.get().version(), "Hibernate debió iniciar la versión en 0 automáticamente");
    }

    @Test
    @DisplayName("Debe lanzar ObjectOptimisticLockingFailureException en H2 por conflicto de versión")
    void shouldThrowRealOptimisticLockingException() {
        ArtistEntity artista = new ArtistEntity();
        artista.setNombre("Metallica");
        ArtistEntity artistaGuardado = artistaJpaRepository.saveAndFlush(artista);

        AlbumEntity album = new AlbumEntity();
        album.setNombre("Master of Puppets");
        album.setArtista(artistaGuardado);
        AlbumEntity albumGuardado = albumJpaRepository.saveAndFlush(album);

        StyleEntity estilo = new StyleEntity();
        estilo.setEstilo("Rock");
        StyleEntity estiloGuardado = estiloMusicalJpaRepository.saveAndFlush(estilo);

        Album albumDomain = new Album(albumGuardado.getId(), albumGuardado.getNombre(), null, null, 0);
        Style estiloDomain = new Style(estiloGuardado.getId(), estiloGuardado.getEstilo());

        SongEntity entity = new SongEntity();
        entity.setTitulo("Enter Sandman");
        entity.setDuracion(300);
        entity.setCantRepro(0);
        entity.setAlbum(albumGuardado);
        entity.setEstiloMusical(estiloGuardado);
        SongEntity entidadPersistida = cancionJpaRepository.saveAndFlush(entity);

        Song instanciaUsuarioA = new Song(
                entidadPersistida.getId(),
                "Enter Sandman Modificado A",
                300,
                0,
                albumDomain,
                estiloDomain,
                null,
                entidadPersistida.getVersion()
        );
        Song instanciaUsuarioB = new Song(
                entidadPersistida.getId(),
                "Enter Sandman Modificado B",
                300,
                0,
                albumDomain,
                estiloDomain,
                null,
                entidadPersistida.getVersion()
        );

        cancionRepository.save(instanciaUsuarioA);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            cancionRepository.save(instanciaUsuarioB);
            testEntityManager.flush();
        }, "La base de datos H2 debió rechazar la transacción por versión obsoleta");
    }
}
