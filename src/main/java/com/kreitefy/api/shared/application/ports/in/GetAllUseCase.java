package com.kreitefy.api.shared.application.ports.in;

import java.util.List;
@org.springframework.modulith.NamedInterface
public interface GetAllUseCase<T> {
    List<T> getAll();
}
