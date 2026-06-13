package com.kreitefy.api.product.infrastructure.rest;

import com.kreitefy.api.product.domain.models.Style;
import com.kreitefy.api.product.infrastructure.mappers.StyleMapper;
import com.kreitefy.api.product.infrastructure.rest.dtos.response.StyleResponseDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.shared.application.services.CrudPageableService;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.shared.domain.models.PageInfo;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/styles")
public class StyleRestController {
    private final StyleMapper estiloMusicalMapper;
    private final CrudPageableService<Style, Long, Void> estiloMusicalCrudService;

    public StyleRestController(StyleMapper estiloMusicalMapper,
                               CrudPageableService<Style, Long, Void> estiloMusicalCrudService) {
        this.estiloMusicalMapper = estiloMusicalMapper;
        this.estiloMusicalCrudService = estiloMusicalCrudService;
    }

    @GetMapping
    public ResponseEntity<List<StyleResponseDto>> getAllEstilos(){
        List<Style> estilos = estiloMusicalCrudService.getAll();
        List<StyleResponseDto> estilosDto = estiloMusicalMapper.toDtoList(estilos);
        return ResponseEntity.ok(estilosDto);
    }

    @GetMapping(value = "/backoffice", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponseDto<StyleResponseDto>> getEstilosBackoffice(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {

        Optional<PageInfo> pageInfo = PageInfo.of(page,size);

        Page<Style> pageDominio = estiloMusicalCrudService.findByCriteria(null, pageInfo);

        PagedResponseDto<StyleResponseDto> response = new PagedResponseDto<>(
                pageDominio.getContent().stream()
                        .map(estiloMusicalMapper::domainToDto)
                        .toList(),
                pageDominio.getTotalElements(),
                pageDominio.getTotalPages(),
                pageDominio.getNumber() + 1,
                pageDominio.getSize()
        );
        return ResponseEntity.ok(response);
    }

    // Obtener un estilo por ID
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StyleResponseDto> getEstiloById(@PathVariable Long id) {
        return ResponseEntity.ok(estiloMusicalCrudService.findById(id)
                .map(estiloMusicalMapper::domainToDto)
                .orElseThrow(() -> new NotFoundException("Estilo no encontrado con ID: " + id)));
    }

    // Actualizar un estilo
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StyleResponseDto> actualizarEstilo(@PathVariable Long id,
                                                             @RequestBody StyleResponseDto estiloMusicalDto) {
        estiloMusicalCrudService.findById(id)
                .orElseThrow(() -> new NotFoundException("Estilo no encontrado con ID: " + id));

        Style estiloMusical = estiloMusicalMapper.dtoToDomain(estiloMusicalDto);
        Style actualizado = estiloMusicalCrudService.actualizar(estiloMusical);
        StyleResponseDto resultadoDto = estiloMusicalMapper.domainToDto(actualizado);
        return ResponseEntity.ok(resultadoDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteEstilo(@PathVariable Long id) {
        estiloMusicalCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StyleResponseDto> crearEstilo(@RequestBody StyleResponseDto estiloMusicalDto) {
        Style estiloMusical = estiloMusicalMapper.dtoToDomain(estiloMusicalDto);
        Style creado = estiloMusicalCrudService.crear(estiloMusical);
        StyleResponseDto resultadoDto = estiloMusicalMapper.domainToDto(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultadoDto);
    }
}
