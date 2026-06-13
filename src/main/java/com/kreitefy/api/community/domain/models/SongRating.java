package com.kreitefy.api.community.domain.models;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.product.domain.models.Song;

public record SongRating(
        User username,
        Song cancion,
        Integer valoracion
) { }