package com.kreitefy.api.product.domain.criteria;

public record SongCriteria(
        String titulo,
        String nombreArtista,
        String nombreAlbum,
        String estilo
) {
}