package com.kreitefy.api.community.infrastructure.persistence.jpa;

import com.kreitefy.api.community.infrastructure.persistence.entity.FavoriteSongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteSongJpaRepository extends JpaRepository<FavoriteSongEntity, Long> {
    Page<FavoriteSongEntity> findByUser_Username(String username, Pageable pageable);
    void deleteByUser_UsernameAndSong_Id(String username, Long songId);
    boolean existsByUser_UsernameAndSong_Id(String username, Long songId);
}
