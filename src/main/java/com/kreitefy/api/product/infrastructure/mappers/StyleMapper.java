package com.kreitefy.api.product.infrastructure.mappers;

import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.StyleResponseDto;
import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StyleMapper extends EntityMapper<Style, StyleResponseDto, StyleEntity> {
}
