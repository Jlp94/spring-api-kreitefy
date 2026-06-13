package com.kreitefy.api.product.application.ports.in;

import com.kreitefy.api.product.application.dtos.SongDetailDto;

public interface GetSongDetailUseCase {
    SongDetailDto getSongDetail(Long cancionId, String username);
}
