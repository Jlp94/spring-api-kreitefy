package com.kreitefy.api.product.infrastructure.mappers;

import com.kreitefy.api.product.infrastructure.rest.dtos.response.AlbumDto;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.product.infrastructure.persistence.entity.AlbumEntity;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlbumMapper extends EntityMapper<Album, AlbumDto, AlbumEntity> {

    @Override
    @Mapping(target = "idArtista", source = "artista.id")
    @Mapping(target = "nombreArtista", source = "artista.nombre")
    AlbumDto domainToDto(Album domain);

    @Override
    @Mapping(target = "artista.id", source = "idArtista")
    @Mapping(target = "artista.nombre", ignore = true)
    @Mapping(target = "artista.version", ignore = true)
    Album dtoToDomain(AlbumDto dto);


    @Override
    @Mapping(target = "artista.id", source = "artista.id")
    @Mapping(target = "artista.nombre", source = "artista.nombre")
    @Mapping(target = "artista.version", source = "artista.version")
    @Mapping(target = "version", source = "version")
    Album entityToDomain(AlbumEntity entity);

    @Override
    @Mapping(target = "artista.id", source = "artista.id")
    @Mapping(target = "version", source = "version")
    AlbumEntity domainToEntity(Album domain);
}