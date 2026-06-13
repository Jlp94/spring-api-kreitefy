package com.kreitefy.api.product.infrastructure.persistence.jpa;

import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long>, JpaSpecificationExecutor<AlbumEntity> {
    boolean existsByArtista_Id(Long id);
}
