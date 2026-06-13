package com.kreitefy.api.community.domain.models;

import java.time.LocalDateTime;

public record FavoriteSong(
        Long id,
        String username,
        Long songId,
        LocalDateTime createdAt
) {
    public static FavoriteSong create(String username, Long songId) {
        return new FavoriteSong(null, username, songId, LocalDateTime.now());
    }
}
