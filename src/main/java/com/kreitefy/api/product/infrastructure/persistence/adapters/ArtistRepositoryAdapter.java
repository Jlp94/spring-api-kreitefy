package com.kreitefy.api.product.infrastructure.persistence.adapters;

import com.kreitefy.api.product.application.ports.out.ArtistRepositoryPort;
import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.domain.criteria.ArtistCriteria;
import com.kreitefy.api.product.infrastructure.mappers.ArtistMapper;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.persistence.jpa.ArtistJpaRepository;
import com.kreitefy.api.shared.infrastructure.persistence.spec.LikeSpecification;
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
public class ArtistRepositoryAdapter implements ArtistRepositoryPort {
    private final ArtistJpaRepository artistaJpaRepository;
    private final ArtistMapper artistaMapper;


    public ArtistRepositoryAdapter(ArtistJpaRepository artistaJpaRepository, ArtistMapper artistaMapper) {
        this.artistaJpaRepository = artistaJpaRepository;
        this.artistaMapper = artistaMapper;
    }

    @Override
    public Artist save(Artist artista) {
        if (artista.id() != null) {
            ArtistEntity existente = artistaJpaRepository.findById(artista.id()).orElse(null);
            if (existente != null) {
                if (artista.version() != null && artista.version() < existente.getVersion()) {
                    throw new ObjectOptimisticLockingFailureException(ArtistEntity.class, artista.id());
                }
                existente.setNombre(artista.nombre());
                existente.setVersion(artista.version());
                ArtistEntity artistaGuardado = artistaJpaRepository.save(existente);
                return artistaMapper.entityToDomain(artistaGuardado);
            }
        }
        ArtistEntity entity = artistaMapper.domainToEntity(artista);
        ArtistEntity savedEntity = artistaJpaRepository.save(entity);
        return artistaMapper.entityToDomain(savedEntity);
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return artistaJpaRepository.findById(id).map(artistaMapper::entityToDomain);
    }

    @Override
    public void delete(Long id) {
        artistaJpaRepository.deleteById(id);
    }

    @Override
    public List<Artist> findAll() {
        List<ArtistEntity> artistasEntity = artistaJpaRepository.findAll();
        return this.artistaMapper.toDomainListFromEntity(artistasEntity);
    }

    @Override
    public Page<Artist> findByCriteria(ArtistCriteria criteria, Optional<PageInfo> pageInfo) {

        LikeSpecification<ArtistEntity> predicateByNombre = new LikeSpecification<>("nombre", criteria.nombre());
        Specification<ArtistEntity> spec = Specification.unrestricted();
        spec = spec.and(predicateByNombre);

        Sort sort = Sort.by("id").ascending();

        Pageable pageable = pageInfo
                .map(p -> {
                    int pageIndex = (p.page() > 0) ? p.page() - 1 : 0;
                    return PageRequest.of(pageIndex, p.pageSize(), sort);
                })
                .orElseGet(() -> PageRequest.of(0, 10, sort));

        Page<ArtistEntity> entityPage = this.artistaJpaRepository.findAll(spec, pageable);

        return entityPage.map(this.artistaMapper::entityToDomain);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return artistaJpaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        return artistaJpaRepository.existsByNombreAndIdNot(nombre, id);
    }
}