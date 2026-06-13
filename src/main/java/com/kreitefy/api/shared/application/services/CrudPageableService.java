package com.kreitefy.api.shared.application.services;

import com.kreitefy.api.shared.application.ports.in.GetAllPaginadoUseCase;
@org.springframework.modulith.NamedInterface
public interface CrudPageableService<T,ID,C>  extends GetAllPaginadoUseCase<T,C>,CrudService<T,ID> {
}
