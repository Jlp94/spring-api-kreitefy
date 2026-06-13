package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.ports.out.ArtistRepositoryPort;
import com.kreitefy.api.product.application.ports.out.SongRepositoryPort;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.product.domain.criteria.AlbumCriteria;
import com.kreitefy.api.shared.domain.errors.ConflictException;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.application.ports.out.AlbumRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumCrudService implements CrudPageableService<Album, Long, AlbumCriteria> {

    private final AlbumRepositoryPort albumRepositoryPort;
    private final SongRepositoryPort cancionRepositoryPort;
    private final ArtistRepositoryPort artistaRepositoryPort;

    public AlbumCrudService(AlbumRepositoryPort albumRepositoryPort,
                            SongRepositoryPort cancionRepositoryPort,
                            ArtistRepositoryPort artistaRepositoryPort) {
        this.albumRepositoryPort = albumRepositoryPort;
        this.cancionRepositoryPort = cancionRepositoryPort;
        this.artistaRepositoryPort = artistaRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Album> findById(Long id) {
        return this.albumRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public Album crear(Album album) {
        this.artistaRepositoryPort.findById(album.artista().id())
                .orElseThrow(() -> new NotFoundException("No se puede crear el álbum: El artista con ID " + album.artista().id() + " no existe en el sistema."));
        return this.albumRepositoryPort.save(album);
    }

    @Override
    @Transactional
    public Album actualizar(Album album) {
        this.albumRepositoryPort.findById(album.id())
                .orElseThrow(() -> new NotFoundException("No se puede actualizar: El álbum con ID " + album.id() + "  no existe."));
        this.artistaRepositoryPort.findById(album.artista().id())
                .orElseThrow(() -> new NotFoundException("No se puede actualizar el álbum: El artista con ID " + album.artista().id() + " no existe en el sistema."));

        return this.albumRepositoryPort.save(album);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean tieneCanciones = this.cancionRepositoryPort.existsByAlbum_Id(id);
        if (tieneCanciones) {
            throw new ConflictException("No se puede eliminar este album ya que contiene canciones.");
        }
        this.albumRepositoryPort.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Album> findByCriteria(AlbumCriteria criteria, Optional<PageInfo> pageInfo) {
        return this.albumRepositoryPort.findByCriteria(criteria, pageInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Album> getAll() {
        return this.albumRepositoryPort.findAll();
    }
}
