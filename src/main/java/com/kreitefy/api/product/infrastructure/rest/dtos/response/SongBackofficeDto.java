package com.kreitefy.api.product.infrastructure.rest.dtos.response;

import java.time.LocalDateTime;

public record SongBackofficeDto(
        Long id,
        String titulo,
        Long idAlbum,
        String nombreAlbum,
        String nombreArtista,
        Long idEstiloMusical,
        String estiloMusical,
        Integer duracion,
        Integer cantRepro,
        LocalDateTime fechaCreacion,
        Integer version
) {
}
