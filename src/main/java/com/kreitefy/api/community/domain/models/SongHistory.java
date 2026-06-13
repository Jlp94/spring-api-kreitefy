package com.kreitefy.api.community.domain.models;

import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.users.domain.models.User;

import java.time.LocalDateTime;

public record SongHistory(
        Long id,
        User username,
        Song cancion,
        LocalDateTime fechaReproduccion
) { }