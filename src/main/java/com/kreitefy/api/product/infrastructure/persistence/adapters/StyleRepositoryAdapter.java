package com.kreitefy.api.product.infrastructure.persistence.adapters;

import com.kreitefy.api.product.application.ports.out.StyleRepositoryPort;
import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.product.infrastructure.mappers.StyleMapper;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.persistence.entity.StyleEntity;
import com.kreitefy.api.product.infrastructure.persistence.jpa.StyleJpaRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StyleRepositoryAdapter implements StyleRepositoryPort {

    private final StyleJpaRepository estiloJpaRepository;
    private final StyleMapper estiloMusicalMapper;

    public StyleRepositoryAdapter(StyleJpaRepository estiloJpaRepository,
                                  StyleMapper estiloMusicalMapper) {
        this.estiloJpaRepository = estiloJpaRepository;
        this.estiloMusicalMapper = estiloMusicalMapper;
    }


    @Override
    public Style save(Style estilo) {
        return estiloMusicalMapper
                .entityToDomain(estiloJpaRepository
                        .save(estiloMusicalMapper.domainToEntity(estilo)));
    }

    @Override
    public Optional<Style> findById(Long id) {
        return estiloJpaRepository.findById(id)
                .map(estiloMusicalMapper::entityToDomain);
    }

    @Override
    public void delete(Long id) {
        this.estiloJpaRepository.deleteById(id);
    }

    @Override
    public List<Style> findAll() {
        return estiloMusicalMapper
                .toDomainListFromEntity(estiloJpaRepository
                        .findAll());
    }

    @Override
    public Page<Style> findAll(Optional<PageInfo> pageInfo) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = pageInfo
                .map(p -> {
                    int pageIndex = (p.page() > 0) ? p.page() - 1 : 0;
                    return PageRequest.of(pageIndex, p.pageSize(), sort);
                })
                .orElse(PageRequest.of(0, 20, sort));

        Page<StyleEntity> entityPage = estiloJpaRepository.findAll(pageable);

        return entityPage.map(estiloMusicalMapper::entityToDomain);
    }
}
