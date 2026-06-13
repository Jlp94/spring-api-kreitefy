package com.kreitefy.api.product.domain.models;

import java.time.LocalDateTime;

public record Song(
        Long id,
        String titulo,
        Integer duracion,
        Integer cantRepro,
        Album  album,
        Style estiloMusical,
        LocalDateTime fechaCreacion,
        Integer version
) {
    public Song withIncrementedReproducciones() {
        return new Song(
                this.id,
                this.titulo,
                this.duracion,
                this.cantRepro + 1,
                this.album,
                this.estiloMusical,
                this.fechaCreacion,
                this.version
        );
    }
}