package com.kreitefy.api.shared.domain.models;

import java.util.Optional;
@org.springframework.modulith.NamedInterface
public record PageInfo(
        Integer page,
        Integer pageSize
) {
    public PageInfo {
        if (pageSize == null) {
            pageSize = 20;
        }
    }

    public static Optional<PageInfo> of(Integer page, Integer pageSize) {
        if (page == null) {
            return Optional.empty();
        }
        return Optional.of(new PageInfo(page, pageSize));
    }
}