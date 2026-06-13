package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.ports.out.AlbumRepositoryPort;
import com.kreitefy.api.product.application.ports.out.ArtistRepositoryPort;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.shared.domain.errors.ConflictException;
import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.domain.criteria.ArtistCriteria;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistCrudService implements CrudPageableService<Artist,Long, ArtistCriteria> {

    private final ArtistRepositoryPort artistaRepositoryPort;
    private final AlbumRepositoryPort albumRepositoryPort;

    public ArtistCrudService(ArtistRepositoryPort artistaRepositoryPort, AlbumRepositoryPort albumRepositoryPort) {
        this.artistaRepositoryPort = artistaRepositoryPort;
        this.albumRepositoryPort = albumRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Artist> findById(Long id) {
        return this.artistaRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public Artist crear(Artist domain) {
        if (artistaRepositoryPort.existsByNombre(domain.nombre())) {
            throw new ConflictException("Ya existe un artista con el nombre: " + domain.nombre());
        }
        return this.artistaRepositoryPort.save(domain);
    }

    @Override
    @Transactional
    public Artist actualizar(Artist domain) {
        if (artistaRepositoryPort.existsByNombreAndIdNot(domain.nombre(), domain.id())) {
            throw new ConflictException("Ya existe otro artista con el nombre: " + domain.nombre());
        }
        return this.artistaRepositoryPort.save(domain);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (albumRepositoryPort.existsByArtista_Id(id)) {
            throw new ConflictException("No se puede eliminar el artista porque tiene álbumes asociados.");
        }
        this.artistaRepositoryPort.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Artist> findByCriteria(ArtistCriteria criteria, Optional<PageInfo> pageInfo) {
        return this.artistaRepositoryPort.findByCriteria(criteria, pageInfo);
    }

    @Override
    public List<Artist> getAll() {
        return this.artistaRepositoryPort.findAll();
    }
}
