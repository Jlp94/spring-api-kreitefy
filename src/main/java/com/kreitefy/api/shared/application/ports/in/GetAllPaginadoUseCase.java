package com.kreitefy.api.shared.application.ports.in;

import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;
@org.springframework.modulith.NamedInterface
public interface GetAllPaginadoUseCase<T,C> {
    Page<T> findByCriteria(C criteria, Optional<PageInfo> pageInfo);
}
