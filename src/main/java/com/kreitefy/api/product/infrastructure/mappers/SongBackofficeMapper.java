package com.kreitefy.api.product.infrastructure.mappers;

import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.SongBackofficeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongBackofficeMapper {

    @Mapping(target = "idAlbum", source = "album.id")
    @Mapping(target = "nombreAlbum", source = "album.nombre")
    @Mapping(target = "nombreArtista", source = "album.artista.nombre")
    @Mapping(target = "idEstiloMusical", source = "estiloMusical.id")
    @Mapping(target = "estiloMusical", source = "estiloMusical.estilo")
    SongBackofficeDto domainToDto(Song cancion);

    List<SongBackofficeDto> toDtoList(List<Song> cancionesDominio);
}



