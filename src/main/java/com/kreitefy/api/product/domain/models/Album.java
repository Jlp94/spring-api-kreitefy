package com.kreitefy.api.product.domain.models;

public record Album(
        Long id,
        String nombre,
        String imagen,
        Artist artista,
        Integer version
) { }
