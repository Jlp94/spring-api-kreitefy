package com.kreitefy.api.community.application.dtos;

import java.time.LocalDateTime;

public record HistoryDetailDto(
        Long id,
        String tituloCancion,
        String nombreArtista,
        LocalDateTime fechaReproduccion
) { }
