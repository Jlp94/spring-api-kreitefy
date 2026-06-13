package com.kreitefy.api.community.infrastructure.persistence.dtos;

public record SongRatingDto(
        Long id,
        String username,
        Long idCancion,
        Integer rating,
        Integer version
) {
}
