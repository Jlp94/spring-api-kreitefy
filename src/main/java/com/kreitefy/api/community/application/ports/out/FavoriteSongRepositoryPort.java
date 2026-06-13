package com.kreitefy.api.community.application.ports.out;

import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface FavoriteSongRepositoryPort {
    FavoriteSong save(FavoriteSong favoriteSong);
    void delete(String username, Long songId);
    Page<FavoriteSong> findByUser(String username, Optional<PageInfo> pageInfo);
    boolean exists(String username, Long songId);
}
