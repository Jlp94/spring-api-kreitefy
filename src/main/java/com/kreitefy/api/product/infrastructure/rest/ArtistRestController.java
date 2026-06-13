package com.kreitefy.api.product.infrastructure.rest;

import com.kreitefy.api.product.infrastructure.rest.dtos.request.ArtistUpdateRequestDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.product.domain.models.Artist;
import com.kreitefy.api.product.domain.criteria.ArtistCriteria;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.mappers.ArtistMapper;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
public class ArtistRestController {

    private final ArtistMapper artistaMapper;
    private final CrudPageableService<Artist, Long, ArtistCriteria> artistaCrudService;
    public ArtistRestController(ArtistMapper artistaMapper, CrudPageableService<Artist, Long, ArtistCriteria> artistaCrudService) {
        this.artistaMapper = artistaMapper;
        this.artistaCrudService = artistaCrudService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<PagedResponseDto<ArtistUpdateRequestDto>> getAllCriteriaArtistas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        ArtistCriteria criteria = new ArtistCriteria(nombre);

        Optional<PageInfo> pageInfo = PageInfo.of(page,size);

        Page<Artist> pageDominio = this.artistaCrudService.findByCriteria(criteria, pageInfo);

        PagedResponseDto<ArtistUpdateRequestDto> response = new PagedResponseDto<>(
                pageDominio.getContent().stream().map(artistaMapper::domainToDto).toList(),
                pageDominio.getTotalElements(),
                pageDominio.getTotalPages(),
                pageDominio.getNumber() + 1,
                pageDominio.getSize()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ArtistUpdateRequestDto> createArtista(@RequestBody ArtistUpdateRequestDto artistaDto){
        Artist artista = artistaMapper.dtoToDomain(artistaDto);
        Artist creado = artistaCrudService.crear(artista);

        return ResponseEntity.status(HttpStatus.CREATED).body(artistaMapper.domainToDto(creado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtista(@PathVariable Long id) {
        artistaCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistUpdateRequestDto> updateArtista(@PathVariable Long id, @RequestBody ArtistUpdateRequestDto artistaDto){
        Artist artista = artistaCrudService.findById(id)
                .orElseThrow(() -> new NotFoundException("Artista no encontrado con ID: " + id));

        Artist artistaParaActualizar = artistaMapper.dtoToDomain(artistaDto);

        Artist actualizado = artistaCrudService.actualizar(artistaParaActualizar);

        return ResponseEntity.ok(artistaMapper.domainToDto(actualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/all", produces = "application/json")
    public ResponseEntity<List<ArtistUpdateRequestDto>> getAllArtistas(){
        List<ArtistUpdateRequestDto> artistas = artistaCrudService.getAll()
                .stream()
                .map(artistaMapper::domainToDto)
                .toList();
        return ResponseEntity.ok(artistas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<ArtistUpdateRequestDto> getArtista(@PathVariable Long id){
        return ResponseEntity
                .ok(artistaCrudService
                .findById(id)
                .map(artistaMapper::domainToDto)
                .orElseThrow(()-> new NotFoundException("Artista no encontrado con ID: " + id)));
    }




}
