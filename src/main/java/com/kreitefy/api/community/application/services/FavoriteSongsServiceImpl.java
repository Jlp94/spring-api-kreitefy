package com.kreitefy.api.community.application.services;

import com.kreitefy.api.community.application.ports.in.ManageFavoriteSongsUseCase;
import com.kreitefy.api.community.application.ports.out.FavoriteSongRepositoryPort;
import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.users.domain.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FavoriteSongsServiceImpl implements ManageFavoriteSongsUseCase {
    private final FavoriteSongRepositoryPort favoriteSongRepositoryPort;

    public FavoriteSongsServiceImpl(FavoriteSongRepositoryPort favoriteSongRepositoryPort) {
        this.favoriteSongRepositoryPort = favoriteSongRepositoryPort;
    }

    @Override
    @Transactional
    public FavoriteSong addFavorite(String username, Long songId) {
        if (favoriteSongRepositoryPort.exists(username, songId)) {
            return FavoriteSong.create(username, songId);
        }
        return favoriteSongRepositoryPort.save(FavoriteSong.create(username, songId));
    }

    @Override
    @Transactional
    public void removeFavorite(String username, Long songId) {
        favoriteSongRepositoryPort.delete(username, songId);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<FavoriteSong> getFavorites(String username, Optional<PageInfo> pageInfo) {
        return favoriteSongRepositoryPort.findByUser(username, pageInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(String username, Long songId) {
        return favoriteSongRepositoryPort.exists(username, songId);
    }
}
