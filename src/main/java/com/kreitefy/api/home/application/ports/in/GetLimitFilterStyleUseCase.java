package com.kreitefy.api.home.application.ports.in;

import com.kreitefy.api.product.application.dtos.SongHomeDto;

import java.util.List;

public interface GetLimitFilterStyleUseCase {
    List<SongHomeDto> getLimitAndStyle(int limit, String estilo, String filtro);
}
