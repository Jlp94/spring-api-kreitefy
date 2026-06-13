package com.kreitefy.api.users.infrastructure.mappers;

import com.kreitefy.api.users.domain.models.User;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import com.kreitefy.api.users.infrastructure.rest.dtos.request.UserDto;
import com.kreitefy.api.shared.infrastructure.mappers.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserDto, UserEntity> {
}
