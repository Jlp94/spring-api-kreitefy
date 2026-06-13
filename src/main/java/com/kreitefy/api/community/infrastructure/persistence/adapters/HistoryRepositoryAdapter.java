package com.kreitefy.api.community.infrastructure.persistence.adapters;

import com.kreitefy.api.community.application.ports.out.HistoryRepositoryPort;
import com.kreitefy.api.community.domain.models.SongHistory;
import com.kreitefy.api.community.infrastructure.mappers.SongHistoryMapper;
import com.kreitefy.api.community.infrastructure.persistence.entity.SongHistoryEntity;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.community.infrastructure.persistence.jpa.SongHistoryJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class HistoryRepositoryAdapter implements HistoryRepositoryPort {
    private final SongHistoryMapper historialDetalleMapper;
    private final SongHistoryJpaRepository historialCancionJpaRepository;

    public HistoryRepositoryAdapter(SongHistoryMapper historialDetalleMapper, SongHistoryJpaRepository historialCancionJpaRepository) {
        this.historialDetalleMapper = historialDetalleMapper;
        this.historialCancionJpaRepository = historialCancionJpaRepository;
    }

    @Override
    public Page<SongHistory> findByUsername(String username, Optional<PageInfo> pageInfo) {
        Sort sort = Sort.by("fechaReproduccion").descending();

        Pageable pageable = pageInfo
                .map(p -> {
                    int pageIndex = (p.page() > 0) ? p.page() - 1 : 0;
                    return PageRequest.of(pageIndex, p.pageSize(), sort);
                })
                .orElseGet(() -> PageRequest.of(0, 20, sort));

        Page<SongHistoryEntity> entityPage = historialCancionJpaRepository.findByUsername_Username(username, pageable);

        return entityPage.map(historialDetalleMapper::entityToDomain);
    }

}
