package com.kreitefy.api.community.infrastructure.mappers;

import com.kreitefy.api.community.infrastructure.persistence.dtos.SongHistoryDto;
import com.kreitefy.api.community.domain.models.SongHistory;
import com.kreitefy.api.community.infrastructure.persistence.entity.SongHistoryEntity;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongHistoryMapper extends EntityMapper<SongHistory, SongHistoryDto, SongHistoryEntity> {

    @Override
    @Mapping(target = "username", source = "username.username")
    @Mapping(target = "cancion", source = "cancion.id")
    SongHistoryDto domainToDto(SongHistory domain);

    @Override
    @Mapping(target = "username.username", source = "username")
    @Mapping(target = "cancion.id", source = "cancion")
    SongHistory dtoToDomain(SongHistoryDto dto);
}