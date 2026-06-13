package com.kreitefy.api.product.application.ports.out;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SongActivityPort {

    Optional<Integer> getSongRating(Long cancionId, String username);
    void registerPlayback(Long cancionId, String username, LocalDateTime fecha);
    void saveRating(Long cancionId, String username, int valoracion);
}
