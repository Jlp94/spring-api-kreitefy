package com.kreitefy.api.product.application.ports.out;

import com.kreitefy.api.product.domain.criteria.AlbumCriteria;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.shared.application.ports.out.CrudRepository;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AlbumRepositoryPort extends CrudRepository<Album,Long> {
    Page<Album> findByCriteria(AlbumCriteria criteria, Optional<PageInfo> pageInfo);
    boolean existsByArtista_Id(Long id);
}
