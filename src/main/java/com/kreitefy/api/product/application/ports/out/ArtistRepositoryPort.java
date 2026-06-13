package com.kreitefy.api.product.application.ports.out;

import com.kreitefy.api.product.domain.criteria.ArtistCriteria;
import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.shared.application.ports.out.CrudRepository;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ArtistRepositoryPort extends CrudRepository<Artist, Long> {
    Page<Artist> findByCriteria(ArtistCriteria criteria, Optional<PageInfo> pageInfo);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);
}
