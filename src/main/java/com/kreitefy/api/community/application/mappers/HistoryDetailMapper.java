package com.kreitefy.api.community.application.mappers;

import com.kreitefy.api.community.application.dtos.HistoryDetailDto;
import com.kreitefy.api.community.domain.models.SongHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryDetailMapper {

    @Mapping(target = "tituloCancion", source = "cancion.titulo")
    @Mapping(target = "nombreArtista", source = "cancion.album.artista.nombre")
    HistoryDetailDto domainToDto(SongHistory historialCancion);
}
