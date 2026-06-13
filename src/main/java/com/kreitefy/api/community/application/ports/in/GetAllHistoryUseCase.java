package com.kreitefy.api.community.application.ports.in;

import com.kreitefy.api.community.application.dtos.HistoryDetailDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.domain.models.PageInfo;

import java.util.Optional;

public interface GetAllHistoryUseCase {
    PagedResponseDto<HistoryDetailDto> getUserHistory(String username, Optional<PageInfo> pageInfo);
}
