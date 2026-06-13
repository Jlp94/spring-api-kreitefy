package com.kreitefy.api.product.infrastructure.rest.dtos.response;

public record AlbumDto (
        Long id,
        String nombre,
        String imagen,
        Long idArtista,
        String nombreArtista,
        Integer version
) { }
