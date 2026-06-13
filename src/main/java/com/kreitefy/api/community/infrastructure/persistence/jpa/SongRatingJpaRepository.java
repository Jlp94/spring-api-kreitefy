package com.kreitefy.api.community.infrastructure.persistence.jpa;

import com.kreitefy.api.community.infrastructure.persistence.entity.SongRatingEntity;
import com.kreitefy.api.community.infrastructure.persistence.key.SongRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRatingJpaRepository extends JpaRepository<SongRatingEntity, SongRatingKey> {
    Optional<SongRatingEntity> findById(SongRatingKey key);

}
