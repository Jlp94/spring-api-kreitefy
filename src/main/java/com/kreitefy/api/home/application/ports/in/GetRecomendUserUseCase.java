package com.kreitefy.api.home.application.ports.in;

import com.kreitefy.api.home.application.dtos.SongRecommendedDto;

public interface GetRecomendUserUseCase {
    SongRecommendedDto getRecomendacion(String username);
}
