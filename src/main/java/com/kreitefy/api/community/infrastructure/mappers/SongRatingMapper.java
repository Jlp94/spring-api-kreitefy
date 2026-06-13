package com.kreitefy.api.community.infrastructure.mappers;

import com.kreitefy.api.community.domain.models.SongRating;
import com.kreitefy.api.community.infrastructure.persistence.dtos.SongRatingDto;
import com.kreitefy.api.community.infrastructure.persistence.entity.SongRatingEntity;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongRatingMapper extends EntityMapper<SongRating, SongRatingDto, SongRatingEntity> {

    @Override
    @Mapping(target = "username", source = "username.username")
    @Mapping(target = "idCancion", source = "cancion.id")
    SongRatingDto domainToDto(SongRating domain);

    @Override
    @Mapping(target = "username.username", source = "username")
    @Mapping(target = "cancion.id", source = "idCancion")
    SongRating dtoToDomain(SongRatingDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    SongRatingEntity domainToEntity(SongRating domain);
}
