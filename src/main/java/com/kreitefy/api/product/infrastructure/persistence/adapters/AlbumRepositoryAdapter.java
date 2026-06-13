package com.kreitefy.api.product.infrastructure.persistence.adapters;

import com.kreitefy.api.product.application.ports.out.AlbumRepositoryPort;

import com.kreitefy.api.product.domain.criteria.AlbumCriteria;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.mappers.AlbumMapper;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.AlbumJpaRepository;
import com.kreitefy.api.shared.infrastructure.persistence.spec.LikeSpecification;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlbumRepositoryAdapter implements AlbumRepositoryPort {

    private final AlbumJpaRepository albumJpaRepository;
    private final AlbumMapper albumMapper;
    private final EntityManager entityManager;

    AlbumRepositoryAdapter(AlbumJpaRepository albumJpaRepository,
                           AlbumMapper albumMapper,
                           EntityManager entityManager) {
        this.albumJpaRepository = albumJpaRepository;
        this.albumMapper = albumMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Album save(Album album) {
        AlbumEntity entity = albumMapper.domainToEntity(album);

        if (album.artista() != null && album.artista().id() != null) {
            entity.setArtista(entityManager.getReference(ArtistEntity.class, album.artista().id()));
        }

        if (album.id() != null) {
            AlbumEntity existente = albumJpaRepository.findById(album.id()).orElse(null);
            if (existente != null) {
                if (album.version() != null && album.version() < existente.getVersion()) {
                    throw new ObjectOptimisticLockingFailureException(AlbumEntity.class, album.id());
                }
                existente.setNombre(album.nombre());
                existente.setImagen(album.imagen());
                existente.setArtista(entity.getArtista());
                existente.setVersion(album.version());
                AlbumEntity albumGuardado = albumJpaRepository.save(existente);
                return albumMapper.entityToDomain(albumGuardado);
            }
        }

        AlbumEntity albumGuardado = albumJpaRepository.save(entity);
        return albumMapper.entityToDomain(albumGuardado);
    }

    @Override
    public Optional<Album> findById(Long id) {
        return this.albumJpaRepository.findById(id)
                .map(this.albumMapper::entityToDomain);
    }

    @Override
    public void delete(Long id) {
        this.albumJpaRepository.deleteById(id);
    }

    @Override
    public List<Album> findAll() {
        List<AlbumEntity> albumEntities = albumJpaRepository.findAll();
        return this.albumMapper.toDomainListFromEntity(albumEntities);
    }

    @Override
    public Page<Album> findByCriteria(AlbumCriteria criteria, Optional<PageInfo> pageInfo) {
        LikeSpecification<AlbumEntity> predicateByNombre = new LikeSpecification<>("nombre", criteria.nombre());
        LikeSpecification<AlbumEntity> predicateByArtista = new LikeSpecification<>("artista.nombre", criteria.nombreArtista());

        Specification<AlbumEntity> spec = Specification.unrestricted();

        spec = spec.and(predicateByNombre)
                .and(predicateByArtista);

        Sort sort = Sort.by("id").ascending();

        Pageable pageable = pageInfo
                .map(p -> {
                    int pageIndex = (p.page() > 0) ? p.page() - 1 : 0;
                    return PageRequest.of(pageIndex, p.pageSize(), sort);
                })
                .orElseGet(() -> PageRequest.of(0, 20, sort));

        Page<AlbumEntity> entityPage = this.albumJpaRepository.findAll(spec, pageable);

        return entityPage.map(this.albumMapper::entityToDomain);
    }

    @Override
    public boolean existsByArtista_Id(Long id) {
        return this.albumJpaRepository.existsByArtista_Id(id);

    }
}
