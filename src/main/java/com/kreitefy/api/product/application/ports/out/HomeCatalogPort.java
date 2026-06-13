package com.kreitefy.api.product.application.ports.out;

import com.kreitefy.api.product.domain.models.Song;

import java.util.List;

public interface HomeCatalogPort {
    List<Song> findLimitFilter(int limit, String estilo, String filtro);
}
