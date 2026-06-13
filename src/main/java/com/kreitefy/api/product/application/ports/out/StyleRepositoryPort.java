package com.kreitefy.api.product.application.ports.out;


import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.shared.application.ports.out.CrudRepository;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface StyleRepositoryPort extends CrudRepository<Style, Long> {
    Page<Style> findAll(Optional<PageInfo> pageInfo);
}
