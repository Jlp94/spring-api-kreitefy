package com.kreitefy.api.product.infrastructure.rest.dtos.request;

public record ArtistUpdateRequestDto(
        Long id,
        String nombre,
        Integer version
) {
}
