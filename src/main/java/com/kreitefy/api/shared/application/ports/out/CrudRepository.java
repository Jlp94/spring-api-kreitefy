package com.kreitefy.api.shared.application.ports.out;

import java.util.List;
import java.util.Optional;

@org.springframework.modulith.NamedInterface
public interface CrudRepository<T,ID> {
    T save(T domain);
    Optional<T> findById(ID id);
    void delete(ID id);
    List<T> findAll();
}
