package com.kreitefy.api.product.infrastructure.persistence;

import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.infrastructure.persistence.adapters.AlbumRepositoryAdapter;
import com.kreitefy.api.product.domain.criteria.AlbumCriteria;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.mappers.AlbumMapper;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.AlbumJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumRepositoryPortImplTest {

    @Mock
    private AlbumJpaRepository albumJpaRepository;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AlbumRepositoryAdapter albumRepository;

    private Album albumDomain;
    private AlbumEntity albumEntity;

    @BeforeEach
    void setUp() {
        Artist artista = new Artist(1L, "Artista Test", 1);
        albumDomain = new Album(1L, "Album Test", "img.png", artista, 2);

        albumEntity = new AlbumEntity();
        albumEntity.setId(1L);
        albumEntity.setNombre("Album Antiguo");
        albumEntity.setVersion(1);
    }

    @Test
    @DisplayName("Debe actualizar un álbum existente y setear la versión para evitar conflictos (Optimistic Locking)")
    void save_WhenAlbumExists_ShouldUpdateAndSetVersion() {
        AlbumEntity newMappedEntity = new AlbumEntity();
        when(albumMapper.domainToEntity(albumDomain)).thenReturn(newMappedEntity);
        when(entityManager.getReference(eq(ArtistEntity.class), anyLong())).thenReturn(new ArtistEntity());
        when(albumJpaRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(albumJpaRepository.save(any(AlbumEntity.class))).thenReturn(albumEntity);
        when(albumMapper.entityToDomain(any(AlbumEntity.class))).thenReturn(albumDomain);

        Album result = albumRepository.save(albumDomain);

        assertNotNull(result);
        assertEquals(albumDomain.nombre(), result.nombre());
        verify(albumJpaRepository, times(1)).findById(1L);
        assertEquals(2, albumEntity.getVersion(),
                "La versión del cliente debe asignarse a la entidad para el control de concurrencia");
        verify(albumJpaRepository, times(1)).save(albumEntity);
    }

    @Test
    @DisplayName("Debe lanzar ObjectOptimisticLockingFailureException cuando la versión del dominio es menor que la de la base de datos")
    void save_WhenDomainVersionIsLessThanDatabaseVersion_ShouldThrowConflictException() {
        albumEntity.setVersion(5);
        Artist artista = new Artist(1L, "Artista Test", 1);
        Album albumVersionVieja = new Album(1L, "Album Test", "img.png", artista, 3);

        AlbumEntity newMappedEntity = new AlbumEntity();
        when(albumMapper.domainToEntity(albumVersionVieja)).thenReturn(newMappedEntity);
        when(entityManager.getReference(eq(ArtistEntity.class), anyLong())).thenReturn(new ArtistEntity());

        when(albumJpaRepository.findById(1L)).thenReturn(Optional.of(albumEntity));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            albumRepository.save(albumVersionVieja);
        }, "Se esperaba un fallo de bloqueo optimista preventivo");

        verify(albumJpaRepository, times(1)).findById(1L);
        verify(albumJpaRepository, never()).save(any(AlbumEntity.class));
        verify(albumMapper, never()).entityToDomain(any(AlbumEntity.class)); 
    }

    @Test
    @DisplayName("Debe insertar un álbum nuevo desde cero si no tiene ID o no existe")
    void save_WhenAlbumIsNew_ShouldInsert() {
        Album albumNuevo = new Album(null, "Nuevo Album", "new.png", new Artist(1L, "A", 1), 0);
        AlbumEntity entityNuevo = new AlbumEntity();

        when(albumMapper.domainToEntity(albumNuevo)).thenReturn(entityNuevo);
        when(entityManager.getReference(eq(ArtistEntity.class), anyLong())).thenReturn(new ArtistEntity());
        when(albumJpaRepository.save(entityNuevo)).thenReturn(entityNuevo);
        when(albumMapper.entityToDomain(entityNuevo)).thenReturn(albumNuevo);

        Album result = albumRepository.save(albumNuevo);

        assertNotNull(result);
        verify(albumJpaRepository, never()).findById(anyLong());
        verify(albumJpaRepository, times(1)).save(entityNuevo);
    }

    @Test
    @DisplayName("Debe retornar un Optional con el álbum de dominio cuando el ID existe")
    void findById_WhenIdExists_ShouldReturnOptionalWithAlbum() {
        when(albumJpaRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(albumMapper.entityToDomain(albumEntity)).thenReturn(albumDomain);

        Optional<Album> result = albumRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Album Test", result.get().nombre());
        verify(albumJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe eliminar un álbum invocando al repositorio de JPA")
    void delete_ShouldInvokeJpaRepository() {
        doNothing().when(albumJpaRepository).deleteById(1L);

        albumRepository.delete(1L);

        verify(albumJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe retornar la lista completa de álbumes mapeados a dominio")
    void findAll_ShouldReturnMappedAlbumList() {
        List<AlbumEntity> entities = List.of(albumEntity);
        List<Album> domainList = List.of(albumDomain);

        when(albumJpaRepository.findAll()).thenReturn(entities);
        when(albumMapper.toDomainListFromEntity(entities)).thenReturn(domainList);

        List<Album> result = albumRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(albumJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar álbumes por criterios dinámicos y paginarlos correctamente")
    @SuppressWarnings("unchecked")
    void findByCriteria_ShouldReturnPaginatedAlbums() {
        AlbumCriteria criteria = new AlbumCriteria("Pop", "Madonna");
        Page<AlbumEntity> entityPage = new PageImpl<>(List.of(albumEntity));

        when(albumJpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);
        when(albumMapper.entityToDomain(albumEntity)).thenReturn(albumDomain);

        Optional<PageInfo> pageInfoReal = PageInfo.of(1, 20);
        Page<Album> result = albumRepository.findByCriteria(criteria, pageInfoReal);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(albumJpaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Debe devolver verdadero si el artista tiene álbumes vinculados")
    void existsByArtistaId_ShouldReturnTrueWhenExists() {
        when(albumJpaRepository.existsByArtista_Id(1L)).thenReturn(true);

        boolean result = albumRepository.existsByArtista_Id(1L);

        assertTrue(result);
        verify(albumJpaRepository, times(1)).existsByArtista_Id(1L);
    }
}