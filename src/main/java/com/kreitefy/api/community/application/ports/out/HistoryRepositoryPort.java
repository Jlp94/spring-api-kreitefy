package com.kreitefy.api.community.application.ports.out;

import com.kreitefy.api.community.domain.models.SongHistory;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface HistoryRepositoryPort {
    Page<SongHistory> findByUsername(String username, Optional<PageInfo> pageInfo);
}
