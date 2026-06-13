package com.kreitefy.api.home.infrastructure.rest.dtos.response;

import com.kreitefy.api.product.application.dtos.SongHomeDto;

import java.util.List;

public record RecomendUserDto(
        String mensaje,
        List<SongHomeDto> canciones
) {
}
