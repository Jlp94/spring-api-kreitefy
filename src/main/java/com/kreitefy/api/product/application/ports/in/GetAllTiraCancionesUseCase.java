package com.kreitefy.api.product.application.ports.in;

import com.kreitefy.api.product.application.dtos.SongHomeDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.shared.domain.models.PageInfo;

import java.util.Optional;

public interface GetAllTiraCancionesUseCase {
    PagedResponseDto<SongHomeDto> findAll(SongCriteria criteria, Optional<PageInfo> pageInfo);
}
