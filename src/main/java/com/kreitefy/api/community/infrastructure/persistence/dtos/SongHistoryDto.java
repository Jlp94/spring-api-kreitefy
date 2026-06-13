package com.kreitefy.api.community.infrastructure.persistence.dtos;

import java.time.LocalDateTime;

public record SongHistoryDto(
        Long id,
        String username,
        Long cancion,
        LocalDateTime fechaReproduccion,
        Integer version
) {
}
