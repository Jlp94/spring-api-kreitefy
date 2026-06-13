package com.kreitefy.api.shared.application.ports.in;
@org.springframework.modulith.NamedInterface
public interface PostUseCase<T> {
    T crear(T domain);
}
