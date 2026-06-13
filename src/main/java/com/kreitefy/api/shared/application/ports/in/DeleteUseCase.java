package com.kreitefy.api.shared.application.ports.in;
@org.springframework.modulith.NamedInterface

public interface DeleteUseCase<ID> {
    void delete(ID id);
}