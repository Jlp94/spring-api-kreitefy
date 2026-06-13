package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.ports.out.SongRepositoryPort;
import com.kreitefy.api.product.application.ports.out.StyleRepositoryPort;
import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.shared.domain.errors.ConflictException;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StyleCrudService implements CrudPageableService<Style, Long, Void>{

    private final StyleRepositoryPort repository;
    private final SongRepositoryPort cancionRepository;

    public StyleCrudService(StyleRepositoryPort estiloMusicalRepositoryPort, SongRepositoryPort cancionRepository) {
        this.repository = estiloMusicalRepositoryPort;
        this.cancionRepository = cancionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Style> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Style> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Style crear(Style estilo) {
        return this.repository.save(estilo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Style estilo = repository.findById(id)
                .orElseThrow(() -> new ConflictException("No se encontró el estilo con id: " + id));
        boolean tieneCanciones = this.cancionRepository.existsByEstiloMusical_Estilo(estilo.estilo());
        if (tieneCanciones) {
            throw new ConflictException("No se puede eliminar este estilo ya que contiene canciones.");
        }
        this.repository.delete(id);
    }

    @Override
    @Transactional
    public Style actualizar(Style domain) {
        return this.repository.save(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Style> findByCriteria(Void criteria, Optional<PageInfo> pageInfo) {
        return this.repository.findAll(pageInfo);
    }
}