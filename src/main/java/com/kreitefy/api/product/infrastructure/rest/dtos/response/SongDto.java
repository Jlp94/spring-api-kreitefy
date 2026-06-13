package com.kreitefy.api.product.infrastructure.rest.dtos.response;

import java.time.LocalDateTime;

public record SongDto(
        Long id,
        String titulo,
        Integer duracion,
        Integer cantRepro,
        Long idAlbum,
        Long idEstiloMusical,
        LocalDateTime fechaCreacion,
        Integer version
) { }
