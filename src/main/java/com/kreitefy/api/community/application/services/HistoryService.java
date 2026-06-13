package com.kreitefy.api.community.application.services;

import com.kreitefy.api.community.application.mappers.HistoryDetailMapper;
import com.kreitefy.api.community.application.dtos.HistoryDetailDto;
import com.kreitefy.api.community.application.ports.in.GetAllHistoryUseCase;
import com.kreitefy.api.community.application.ports.out.HistoryRepositoryPort;
import com.kreitefy.api.community.domain.models.SongHistory;

import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.domain.models.PageInfo;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class HistoryService implements GetAllHistoryUseCase {
    private final HistoryRepositoryPort historialRepositoryPort;
    private final HistoryDetailMapper historyDetailMapper;

    public HistoryService(HistoryRepositoryPort historialRepositoryPort, HistoryDetailMapper historialDetalleMapper) {
        this.historialRepositoryPort = historialRepositoryPort;
        this.historyDetailMapper = historialDetalleMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<HistoryDetailDto> getUserHistory(String username, Optional<PageInfo> pageInfo) {
        Page<SongHistory> pageDominio = historialRepositoryPort.findByUsername(username, pageInfo);
        return new PagedResponseDto<>(
                pageDominio.getContent().stream().map(historyDetailMapper::domainToDto).toList(),
                pageDominio.getTotalElements(),
                pageDominio.getTotalPages(),
                pageDominio.getNumber() + 1,
                pageDominio.getSize()
        );
    }
}

