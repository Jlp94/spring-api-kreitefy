package com.kreitefy.api.product.infrastructure.persistence.jpa;

import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArtistJpaRepository extends JpaRepository<ArtistEntity, Long>, JpaSpecificationExecutor<ArtistEntity> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);
}
