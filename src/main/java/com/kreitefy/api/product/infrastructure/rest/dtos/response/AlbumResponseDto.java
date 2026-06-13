package com.kreitefy.api.product.infrastructure.rest.dtos.response;

public record AlbumResponseDto(
        Long id,
        String nombre,
        String imagen,
        String nombreArtista
) {
}
