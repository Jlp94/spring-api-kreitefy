package com.kreitefy.api.shared.infrastructure.persistence.spec;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
@org.springframework.modulith.NamedInterface
public class LikeSpecification<T> implements Specification<T> {
    private String name;
    private String value;

    public LikeSpecification(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (value == null || value.isEmpty()) {
            return cb.conjunction();
        }

        Path<?> path = root;
        for (String part : name.split("\\.")) {
            path = path.get(part);
        }

        return cb.like(cb.lower(path.as(String.class)), "%" + value.toLowerCase() + "%");
    }

}
