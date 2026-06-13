package com.kreitefy.api.home.infrastructure.rest.dtos.response;

import com.kreitefy.api.product.application.dtos.SongHomeDto;

import java.util.List;

public record HomeResponseDto(List<SongHomeDto> novedades, List<SongHomeDto> masEscuchadas, RecomendUserDto recomendUserDto) {
}
