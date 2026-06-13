package com.kreitefy.api.community.infrastructure.rest.dtos.response;

public record FavoriteSongDto(
        Long id,
        String username,
        Long songId,
        String songTitle,
        String artistName
) {
}
