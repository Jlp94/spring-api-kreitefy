package com.kreitefy.api.product.infrastructure.persistence.jpa;

import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StyleJpaRepository extends JpaRepository<StyleEntity, Long>, JpaSpecificationExecutor<StyleEntity> {

}
