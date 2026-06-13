package com.kreitefy.api.product.application.dtos;

public record SongDetailDto(
        Long id,
        String tituloCancion,
        Integer duracion,
        Integer cantRepro,
        String nombreArtista,
        String nombreAlbum,
        String imagenAlbum,
        String nombreEstilo,
        Integer valoracionUsuario
) {
    public SongDetailDto withValoracion(Integer nota) {
        return new SongDetailDto(id, tituloCancion, duracion, cantRepro, nombreArtista, nombreAlbum, imagenAlbum, nombreEstilo, nota);
    }
}
