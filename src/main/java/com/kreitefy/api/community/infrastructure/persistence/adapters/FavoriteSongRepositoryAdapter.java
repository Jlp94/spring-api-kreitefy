package com.kreitefy.api.community.infrastructure.persistence.adapters;

import com.kreitefy.api.community.application.ports.out.FavoriteSongRepositoryPort;
import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.community.infrastructure.mappers.FavoriteSongMapper;
import com.kreitefy.api.community.infrastructure.persistence.entity.FavoriteSongEntity;
import com.kreitefy.api.community.infrastructure.persistence.jpa.FavoriteSongJpaRepository;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FavoriteSongRepositoryAdapter implements FavoriteSongRepositoryPort {
    private final FavoriteSongJpaRepository favoriteSongJpaRepository;
    private final FavoriteSongMapper favoriteSongMapper;
    private final EntityManager entityManager;

    public FavoriteSongRepositoryAdapter(FavoriteSongJpaRepository favoriteSongJpaRepository,
                                         FavoriteSongMapper favoriteSongMapper,
                                         EntityManager entityManager) {
        this.favoriteSongJpaRepository = favoriteSongJpaRepository;
        this.favoriteSongMapper = favoriteSongMapper;
        this.entityManager = entityManager;
    }

    @Override
    public FavoriteSong save(FavoriteSong favoriteSong) {
        UserEntity userRef = entityManager.getReference(UserEntity.class, favoriteSong.username());
        SongEntity songRef = entityManager.getReference(SongEntity.class, favoriteSong.songId());
        FavoriteSongEntity entity = new FavoriteSongEntity(userRef, songRef, favoriteSong.createdAt());
        return favoriteSongMapper.entityToDomain(favoriteSongJpaRepository.save(entity));
    }

    @Override
    public void delete(String username, Long songId) {
        favoriteSongJpaRepository.deleteByUser_UsernameAndSong_Id(username, songId);
    }

    @Override
    public Page<FavoriteSong> findByUser(String username, Optional<PageInfo> pageInfo) {
        int page = pageInfo.map(PageInfo::page).orElse(1) - 1;
        int size = pageInfo.map(PageInfo::pageSize).orElse(10);
        Pageable pageable = PageRequest.of(page, size);

        Page<FavoriteSongEntity> entityPage = favoriteSongJpaRepository.findByUser_Username(username, pageable);
        return entityPage.map(favoriteSongMapper::entityToDomain);
    }

    @Override
    public boolean exists(String username, Long songId) {
        return favoriteSongJpaRepository.existsByUser_UsernameAndSong_Id(username, songId);
    }
}
