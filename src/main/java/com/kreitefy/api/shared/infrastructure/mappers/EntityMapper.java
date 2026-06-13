package com.kreitefy.api.shared.infrastructure.mappers;

import java.util.List;
@org.springframework.modulith.NamedInterface
public interface EntityMapper <D, A, E> {
    D dtoToDomain(A dto);
    A domainToDto(D domain);
    D entityToDomain(E entity);
    E domainToEntity(D domain);

    List<D> toDomainListFromDto(List<A> dtoList);
    List<A> toDtoList(List<D> domainList);

    List<E> toEntityList(List<D> domainList);
    List<D> toDomainListFromEntity(List<E> entityList);
}
