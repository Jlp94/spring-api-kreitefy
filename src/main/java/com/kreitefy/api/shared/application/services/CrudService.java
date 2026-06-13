package com.kreitefy.api.shared.application.services;

import com.kreitefy.api.shared.application.ports.in.*;
@org.springframework.modulith.NamedInterface
public interface CrudService<T,ID>  extends GetAllUseCase<T>, PostUseCase<T>, GetFindUseCase<T,ID>, UpdateUseCase<T>, DeleteUseCase<ID> {
}
