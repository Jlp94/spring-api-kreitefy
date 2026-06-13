package com.kreitefy.api.product.infrastructure.rest;

import com.kreitefy.api.product.infrastructure.rest.dtos.response.AlbumDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.product.domain.criteria.AlbumCriteria;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.product.domain.models.Album;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.product.infrastructure.mappers.AlbumMapper;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumRestController {
    private final AlbumMapper albumMapper;
    private final CrudPageableService<Album, Long, AlbumCriteria> albumService;

    public AlbumRestController(AlbumMapper albumMapper, CrudPageableService<Album, Long, AlbumCriteria> albumCrudService) {
        this.albumMapper = albumMapper;
        this.albumService = albumCrudService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<PagedResponseDto<AlbumDto>> getAllPaginadoAlbums(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nombreArtista,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        AlbumCriteria criteria = new AlbumCriteria(nombre, nombreArtista);

        Optional<PageInfo> pageInfo = PageInfo.of(page,size);

        Page<Album> pageDominio = this.albumService.findByCriteria(criteria, pageInfo);


        PagedResponseDto<AlbumDto> response = new PagedResponseDto<>(
                pageDominio.getContent().stream().map(albumMapper::domainToDto).toList(),
                pageDominio.getTotalElements(),
                pageDominio.getTotalPages(),
                pageDominio.getNumber() + 1,
                pageDominio.getSize()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<AlbumDto> crearAlbum(@RequestBody AlbumDto albumDto) {
        Album albumDominio = this.albumMapper.dtoToDomain(albumDto);
        Album nuevoAlbum = this.albumService.crear(albumDominio);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.albumMapper.domainToDto(nuevoAlbum));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AlbumDto> actualizarAlbum(@PathVariable Long id,
                                                    @RequestBody AlbumDto albumDto) {
        AlbumDto albumConIdValidado = new AlbumDto(id, albumDto.nombre(), albumDto.imagen(), albumDto.idArtista(), null,albumDto.version() );
        Album albumDominio = this.albumMapper.dtoToDomain(albumConIdValidado);
        Album albumActualizado = this.albumService.actualizar(albumDominio);
        return ResponseEntity.ok(this.albumMapper.domainToDto(albumActualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarAlbum(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/all", produces = "application/json")
    public ResponseEntity<List<AlbumDto>> getAllAlbumes(){
        List<AlbumDto> albumes = albumService.getAll()
                .stream()
                .map(albumMapper::domainToDto)
                .toList();
        return ResponseEntity.ok(albumes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<AlbumDto> getArtista(@PathVariable Long id){
        return ResponseEntity
                .ok(albumService
                        .findById(id)
                        .map(albumMapper::domainToDto)
                        .orElseThrow(()-> new NotFoundException("Álbum no encontrado con ID: " + id)));
    }
}
