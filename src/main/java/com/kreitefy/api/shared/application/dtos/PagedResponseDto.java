package com.kreitefy.api.shared.application.dtos;

import java.io.Serializable;
import java.util.List;

public record PagedResponseDto<T>(
        List<T> data,
        long totalElements,
        int totalPages,
        int page,
        int size
) implements Serializable { }
