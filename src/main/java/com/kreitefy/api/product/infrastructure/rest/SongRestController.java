package com.kreitefy.api.product.infrastructure.rest;

import com.kreitefy.api.product.infrastructure.mappers.SongBackofficeMapper;
import com.kreitefy.api.product.infrastructure.mappers.SongMapper;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.SongBackofficeDto;
import com.kreitefy.api.product.application.dtos.SongHomeDto;
import com.kreitefy.api.product.application.services.ISongDetailService;
import com.kreitefy.api.product.application.dtos.SongDetailDto;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.SongDto;
import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.application.mappers.SongHomeMapper;
import com.kreitefy.api.product.infrastructure.rest.dtos.request.RatingSongRequestDto;

import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.shared.domain.errors.BadRequestException;
import com.kreitefy.api.shared.domain.errors.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.Optional;


@RestController
@RequestMapping("/songs")
public class SongRestController {
    private final SongMapper cancionMapper;
    private final SongBackofficeMapper cancionBackofficeMapper;
    private final ISongDetailService cancionDetailService;
    private final CrudPageableService<Song, Long, SongCriteria> cancionCrudService;

    public SongRestController(CrudPageableService<Song, Long, SongCriteria> cancionCrudService,
                                SongMapper cancionMapper,
                                SongHomeMapper cancionTiraMapper,
                                SongBackofficeMapper cancionBackofficeMapper,
                                ISongDetailService cancionDetailService
    ) {
        this.cancionMapper = cancionMapper;
        this.cancionCrudService = cancionCrudService;
        this.cancionBackofficeMapper = cancionBackofficeMapper;
        this.cancionDetailService = cancionDetailService;

    }
    // -----------------------------------------------------------------------------------
    // USER
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SongDetailDto> getSong(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(cancionDetailService.getSongDetail(id, principal.getName()));
    }

    @PostMapping(value="/{id}/ratings/me", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> rateSong(
            @PathVariable Long id,
            @RequestBody RatingSongRequestDto valoracion,
            Principal principal) {
        String username = principal.getName();
        cancionDetailService.rateSong(id, username, valoracion.rating());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/{id}/plays", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> registerPlayback(@PathVariable Long id,
                                                            Principal principal) {
        String username = principal.getName();
        cancionDetailService.playSong(id, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<PagedResponseDto<SongHomeDto>> getAllCanciones(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String estilo,
            @RequestParam(required = false) String artista,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        SongCriteria criteria = new SongCriteria(titulo, artista, album, estilo);

        Optional<PageInfo> pageInfo = PageInfo.of(page,size);
        PagedResponseDto<SongHomeDto> response = cancionDetailService.findAll(criteria, pageInfo);

        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------------------------
//CRUD ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongDto> createSong(@RequestBody SongDto cancionDto) {
        Song cancionInput = cancionMapper.dtoToDomain(cancionDto);
        Song cancionCreada = cancionCrudService.crear(cancionInput);
        return new ResponseEntity<>(cancionMapper.domainToDto(cancionCreada), HttpStatus.CREATED);
    }

    @GetMapping(value = "/backoffice", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponseDto<SongBackofficeDto>> getCancionesBackoffice(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String estilo,
            @RequestParam(required = false) String artista,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {

        SongCriteria criteria = new SongCriteria(titulo, artista, album, estilo);
        Optional<PageInfo> pageInfo = PageInfo.of(page,size);

        Page<Song> pageDominio = cancionCrudService.findByCriteria(criteria, pageInfo);

        PagedResponseDto<SongBackofficeDto> response = new PagedResponseDto<>(
                pageDominio.getContent().stream()
                        .map(cancionBackofficeMapper::domainToDto)
                        .toList(),
                pageDominio.getTotalElements(),
                pageDominio.getTotalPages(),
                pageDominio.getNumber() + 1,
                pageDominio.getSize()

        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/backoffice", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongBackofficeDto> getSong(@PathVariable Long id) {
        return ResponseEntity.ok(cancionCrudService.findById(id)
                .map(cancionBackofficeMapper::domainToDto)
                .orElseThrow(() -> new BadRequestException("Cancion no encontrada con ID: " + id)));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongDto> updateSong(@PathVariable Long id, @RequestBody SongDto cancionDto) {
        cancionCrudService.findById(id)
                .orElseThrow(() -> new NotFoundException("Canción no encontrada con ID: " + id));
        Song artistaParaActualizar = cancionMapper.dtoToDomain(cancionDto);
        Song actualizado = cancionCrudService.actualizar(artistaParaActualizar);
        return ResponseEntity.ok(cancionMapper.domainToDto(actualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        cancionCrudService.delete(id);
        return ResponseEntity.noContent().build();

    }
}