package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.ports.in.GetAllTiraCancionesUseCase;
import com.kreitefy.api.product.application.ports.in.GetSongDetailUseCase;
import com.kreitefy.api.product.application.ports.in.PlaySongUseCase;
import com.kreitefy.api.product.application.ports.in.RateSongUseCase;

public interface ISongDetailService extends GetSongDetailUseCase, PlaySongUseCase, RateSongUseCase, GetAllTiraCancionesUseCase {
}
