package com.kreitefy.api.product.application.ports.out;

import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.shared.application.ports.out.CrudRepository;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface SongRepositoryPort extends CrudRepository<Song,Long> {

    List<Song> findLimitFilter(int limit, String estilo, String filtro);

    Optional<Song> findById(Long cancionId);
    Page<Song> findByCriteria(SongCriteria criteria, Optional<PageInfo> pageInfo);
    boolean existsByAlbum_Id(Long id);

    boolean existsByEstiloMusical_Estilo(String estilo);

}
