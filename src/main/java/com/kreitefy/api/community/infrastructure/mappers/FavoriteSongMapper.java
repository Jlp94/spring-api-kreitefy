package com.kreitefy.api.community.infrastructure.mappers;

import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.community.infrastructure.persistence.entity.FavoriteSongEntity;
import com.kreitefy.api.community.infrastructure.rest.dtos.response.FavoriteSongDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteSongMapper {

    @Mapping(target = "songTitle", ignore = true)
    @Mapping(target = "artistName", ignore = true)
    FavoriteSongDto domainToDto(FavoriteSong favoriteSong);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "songId", source = "song.id")
    FavoriteSong entityToDomain(FavoriteSongEntity entity);
}
