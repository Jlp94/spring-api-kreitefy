package com.kreitefy.api.community.application.ports.out;

import com.kreitefy.api.community.domain.models.SongRating;
import com.kreitefy.api.product.domain.models.Song;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserActivityRepositoryPort {

    Optional<SongRating> getRating(Long cancionId, String username);

    void registerPlayback(Long cancionId, String username, LocalDateTime fecha);

    void saveRating(Long cancionId, String username, int valoracion);

    List<String> getUserStyles(String username, int limit);

    List<Song> findRecomendaciones(String username, List<String> estilos, int limit);
}