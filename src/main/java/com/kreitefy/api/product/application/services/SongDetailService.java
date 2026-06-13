package com.kreitefy.api.product.application.services;

import com.kreitefy.api.product.application.dtos.SongDetailDto;
import com.kreitefy.api.product.application.dtos.SongHomeDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.product.application.ports.out.SongRepositoryPort;
import com.kreitefy.api.product.application.ports.out.SongActivityPort;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.shared.domain.errors.BadRequestException;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.product.domain.models.Song;

import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.application.mappers.SongDetailMapper;
import com.kreitefy.api.product.application.mappers.SongHomeMapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SongDetailService implements ISongDetailService {
    private final SongRepositoryPort cancionRepositoryPort;
    private final SongActivityPort songActivityPort;
    private final SongDetailMapper cancionDetalleMapper;
    private final SongHomeMapper cancionTiraMapper;

    public SongDetailService(SongRepositoryPort cancionRepositoryPort,
                            SongActivityPort songActivityPort,
                            SongDetailMapper cancionDetalleMapper,
                            SongHomeMapper cancionTiraMapper){
        this.cancionRepositoryPort = cancionRepositoryPort;
        this.songActivityPort = songActivityPort;
        this.cancionDetalleMapper = cancionDetalleMapper;
        this.cancionTiraMapper = cancionTiraMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SongDetailDto getSongDetail(Long cancionId, String username) {
        Song cancion = cancionRepositoryPort.findById(cancionId)
                .orElseThrow(() -> new NotFoundException("Canción no encontrada"));

        Integer nota = songActivityPort.getSongRating(cancionId, username).orElse(0);
        return cancionDetalleMapper.domainToDto(cancion).withValoracion(nota);
    }

    @Override
    @Transactional
    public void playSong(Long cancionId, String username) {
        Song cancionActual = cancionRepositoryPort.findById(cancionId)
                .orElseThrow(() -> new NotFoundException("Canción no encontrada"));
        Song nuevaCancion = cancionActual.withIncrementedReproducciones();
        cancionRepositoryPort.save(nuevaCancion);
        songActivityPort.registerPlayback(cancionId, username, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void rateSong(Long cancionId, String username, int valoracion) {
        if (valoracion < 0 || valoracion > 4) {
            throw new BadRequestException("La valoración debe ser entre 0 y 4 estrellas.");
        }
        songActivityPort.saveRating(cancionId, username, valoracion);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<SongHomeDto> findAll(SongCriteria criteria, Optional<PageInfo> pageInfo) {

        Page<Song> cancionPage = cancionRepositoryPort.findByCriteria(criteria, pageInfo);

        return new PagedResponseDto<>(
                cancionTiraMapper.toDtoList(cancionPage.getContent()),
                cancionPage.getTotalElements(),
                cancionPage.getTotalPages(),
                cancionPage.getNumber() + 1,
                cancionPage.getSize()
        );
    }
}

