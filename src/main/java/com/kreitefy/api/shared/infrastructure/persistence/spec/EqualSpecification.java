package com.kreitefy.api.shared.infrastructure.persistence.spec;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
@org.springframework.modulith.NamedInterface
public class EqualSpecification<T,V> implements Specification<T> {
    private String name;
    private V value;

    public EqualSpecification(String name, V value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            return cb.conjunction();
        }

        Path<?> path = root;
        for (String part : name.split("\\.")) {
            path = path.get(part);
        }

        return cb.equal(path, value);
    }
}
