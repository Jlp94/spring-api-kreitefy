package com.kreitefy.api.product.application.mappers;

import com.kreitefy.api.product.application.dtos.SongHomeDto;
import com.kreitefy.api.product.domain.models.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongHomeMapper {

    @Mapping(target = "imagen", source = "album.imagen")
    @Mapping(target = "nombreArtista", source = "album.artista.nombre")
    SongHomeDto domainToDto(Song cancion);

    List<SongHomeDto> toDtoList(List<Song> cancionesDominio);
}
