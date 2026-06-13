package com.kreitefy.api.product.application.ports.in;

public interface RateSongUseCase {
    void rateSong(Long cancionId, String username, int valoracion);
}
