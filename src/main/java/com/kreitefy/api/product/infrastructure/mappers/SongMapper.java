package com.kreitefy.api.product.infrastructure.mappers;

import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.SongDto;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, StyleMapper.class})
public interface SongMapper extends EntityMapper<Song, SongDto, SongEntity> {

    @Override
    @Mapping(target = "idAlbum", source = "album.id")
    @Mapping(target = "idEstiloMusical", source = "estiloMusical.id")
    SongDto domainToDto(Song domain);

    @Override
    @Mapping(target = "album.id", source = "idAlbum")
    @Mapping(target = "estiloMusical.id", source = "idEstiloMusical")
    Song dtoToDomain(SongDto dto);
}

