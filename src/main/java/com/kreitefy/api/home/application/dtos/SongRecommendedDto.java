package com.kreitefy.api.home.application.dtos;

import com.kreitefy.api.product.application.dtos.SongHomeDto;

import java.util.List;

public record SongRecommendedDto(
        String mensaje,
        List<SongHomeDto> canciones
) { }
