package com.kreitefy.api.community.application.ports.in;

import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ManageFavoriteSongsUseCase {
    FavoriteSong addFavorite(String username, Long songId);
    void removeFavorite(String username, Long songId);
    Page<FavoriteSong> getFavorites(String username, Optional<PageInfo> pageInfo);
    boolean isFavorite(String username, Long songId);
}
