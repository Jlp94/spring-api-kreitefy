package com.kreitefy.api.product.application.mappers;

import com.kreitefy.api.product.application.dtos.SongDetailDto;
import com.kreitefy.api.product.domain.models.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongDetailMapper {

    @Mapping(target = "tituloCancion", source = "titulo")
    @Mapping(target = "nombreArtista", source = "album.artista.nombre")
    @Mapping(target = "nombreAlbum", source = "album.nombre")
    @Mapping(target = "imagenAlbum", source = "album.imagen")
    @Mapping(target = "nombreEstilo", source = "estiloMusical.estilo")
    @Mapping(target = "valoracionUsuario", ignore = true)
    SongDetailDto domainToDto(Song cancion);
}
