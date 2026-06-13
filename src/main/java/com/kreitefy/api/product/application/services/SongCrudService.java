package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.ports.out.SongRepositoryPort;
import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SongCrudService implements CrudPageableService<Song, Long, SongCriteria> {

    private final SongRepositoryPort cancionRepositoryPort;

    public SongCrudService(SongRepositoryPort cancionRepositoryPort) {
        this.cancionRepositoryPort = cancionRepositoryPort;
    }

    @Override
    @Transactional
    public Song crear(Song cancion) {
        return cancionRepositoryPort.save(cancion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Song> findById(Long cancionId) {
        return cancionRepositoryPort.findById(cancionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Song> getAll() {
        return this.cancionRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Song actualizar(Song cancion) {

        return this.cancionRepositoryPort.save(cancion);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.cancionRepositoryPort.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Song> findByCriteria(SongCriteria criteria, Optional<PageInfo> pageInfo) {
        return this.cancionRepositoryPort.findByCriteria(criteria, pageInfo);
    }
}
