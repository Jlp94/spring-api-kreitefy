package com.kreitefy.api.product.infrastructure.persistence.adapters;

import com.kreitefy.api.product.application.ports.out.HomeCatalogPort;
import com.kreitefy.api.product.application.ports.out.SongRepositoryPort;
import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.product.infrastructure.mappers.SongMapper;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.SongJpaRepository;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.shared.infrastructure.persistence.spec.EqualSpecification;
import com.kreitefy.api.shared.infrastructure.persistence.spec.LikeSpecification;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class SongRepositoryAdapter implements SongRepositoryPort, HomeCatalogPort {
    private final SongJpaRepository songJpaRepository;
    private final EntityManager entityManager;
    private final SongMapper songMapper;


    public SongRepositoryAdapter(SongJpaRepository cancionJpaRepository,
                                 SongMapper cancionMapper,
                                 EntityManager entityManager) {
        this.songJpaRepository = cancionJpaRepository;
        this.songMapper = cancionMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Song save(Song cancion) {
        AlbumEntity albumRef = cancion.album() != null ?
                entityManager.getReference(AlbumEntity.class, cancion.album().id())
                : null;
        StyleEntity estiloRef = cancion.estiloMusical() != null && cancion.estiloMusical().id() != null ?
                entityManager.getReference(StyleEntity.class, cancion.estiloMusical().id())
                : null;
        SongEntity cancionEntity;
        if (cancion.id() != null) {
            SongEntity existente = songJpaRepository.findById(cancion.id())
                    .orElseThrow(() -> new ObjectOptimisticLockingFailureException(SongEntity.class, cancion.id()));
            if (cancion.version() != null && cancion.version() < existente.getVersion()) {
                throw new ObjectOptimisticLockingFailureException(SongEntity.class, cancion.id());
            }
            existente.updateFromDomain(cancion, albumRef, estiloRef);
            cancionEntity = existente;
        } else {
            cancionEntity = new SongEntity(cancion, albumRef, estiloRef);
        }
        SongEntity savedEntity = songJpaRepository.save(cancionEntity);
        return songMapper.entityToDomain(savedEntity);
    }

    @Override
    public void delete(Long id) {
        this.songJpaRepository.deleteById(id);
    }

    @Override
    public List<Song> findAll() {
        return this.songMapper.toDomainListFromEntity(songJpaRepository.findAll());
    }



    private List<Song> getSongs(String estilo, Pageable pageable) {
        List<SongEntity> cancionesEntities = songJpaRepository
                .findAll(new EqualSpecification<>("estiloMusical.estilo", estilo), pageable)
                .getContent();

        return songMapper.toDomainListFromEntity(cancionesEntities);
    }

    @Override
    public Optional<Song> findById(Long cancionId) {
        return songJpaRepository.findById(cancionId)
                .map(songMapper::entityToDomain);
    }

    @Override
    public Page<Song> findByCriteria(SongCriteria criteria, Optional<PageInfo> pageInfo) {

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = pageInfo
                .map(p -> {
                    int pageIndex = (p.page() > 0) ? p.page() - 1 : 0;
                    return PageRequest.of(pageIndex, p.pageSize(), sort);
                })
                .orElse(PageRequest.of(0, 20, sort));

        Page<SongEntity> entityPage = songJpaRepository.findByCriteriaQueryDsl( criteria,pageable);

        return entityPage.map(songMapper::entityToDomain);
    }


    public boolean existsByAlbum_Id(Long id) {
        return songJpaRepository.existsByAlbum_Id(id);
    }

    @Override
    public boolean existsByEstiloMusical_Estilo(String estilo) {
       return this.songJpaRepository.existsByEstiloMusical_Estilo(estilo);
    }

    @Override
    public List<Song> findLimitFilter(int limit, String estilo, String filtro) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(filtro).descending());

        return getSongs(estilo, pageable);
    }
}
