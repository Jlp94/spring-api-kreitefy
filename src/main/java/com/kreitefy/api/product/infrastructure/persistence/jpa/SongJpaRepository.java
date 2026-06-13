package com.kreitefy.api.product.infrastructure.persistence.jpa;

import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SongJpaRepository
        extends JpaRepository<SongEntity, Long>, JpaSpecificationExecutor<SongEntity> {

    @EntityGraph(attributePaths = {"album", "album.artista", "estiloMusical"})
    Page<SongEntity> findAll(Specification<SongEntity> spec, Pageable pageable);

    boolean existsByEstiloMusical_Estilo(String estilo);

    boolean existsByAlbum_Id(Long albumId);

}