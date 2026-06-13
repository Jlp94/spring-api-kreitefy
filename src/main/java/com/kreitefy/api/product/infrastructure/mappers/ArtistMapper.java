package com.kreitefy.api.product.infrastructure.mappers;

import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.infrastructure.persistence.entity.ArtistEntity;
import com.kreitefy.api.product.infrastructure.rest.dtos.request.ArtistUpdateRequestDto;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ArtistMapper extends EntityMapper<Artist, ArtistUpdateRequestDto, ArtistEntity> {

}