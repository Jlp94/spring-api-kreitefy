package com.kreitefy.api.shared.application.ports.in;
@org.springframework.modulith.NamedInterface
public interface UpdateUseCase<T> {
    T actualizar(T domain);
}
