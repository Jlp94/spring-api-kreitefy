package com.kreitefy.api.product.infrastructure.persistence.querys;

import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongRepositoryCustom {
    Page<SongEntity> findByCriteriaQueryDsl(SongCriteria criteria, Pageable pageable);
}
