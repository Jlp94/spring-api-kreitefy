package com.kreitefy.api.shared.application.ports.in;

import java.util.Optional;
@org.springframework.modulith.NamedInterface
public interface GetFindUseCase<T,ID> {
    Optional<T> findById(ID id);
}
