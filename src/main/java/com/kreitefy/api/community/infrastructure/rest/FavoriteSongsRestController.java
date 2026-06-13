package com.kreitefy.api.community.infrastructure.rest;

import com.kreitefy.api.community.application.ports.in.ManageFavoriteSongsUseCase;
import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.community.infrastructure.mappers.FavoriteSongMapper;
import com.kreitefy.api.community.infrastructure.rest.dtos.response.FavoriteSongDto;
import com.kreitefy.api.community.infrastructure.rest.dtos.request.FavoriteRequestDto;
import com.kreitefy.api.shared.domain.models.PageInfo;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/users/me/favorites")
public class FavoriteSongsRestController {
    private final ManageFavoriteSongsUseCase manageFavoriteSongsUseCase;
    private final FavoriteSongMapper favoriteSongMapper;

    public FavoriteSongsRestController(ManageFavoriteSongsUseCase manageFavoriteSongsUseCase,
            FavoriteSongMapper favoriteSongMapper) {
        this.manageFavoriteSongsUseCase = manageFavoriteSongsUseCase;
        this.favoriteSongMapper = favoriteSongMapper;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FavoriteSongDto> addFavorite(Principal principal, @RequestBody FavoriteRequestDto request) {
        FavoriteSong favoriteSong = manageFavoriteSongsUseCase.addFavorite(principal.getName(), request.songId());
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteSongMapper.domainToDto(favoriteSong));
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> removeFavorite(Principal principal, @PathVariable Long songId) {
        manageFavoriteSongsUseCase.removeFavorite(principal.getName(), songId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<PagedResponseDto<FavoriteSongDto>> getFavorites(Principal principal,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Optional<PageInfo> pageInfo = PageInfo.of(page, size);
        Page<FavoriteSong> domainPage = manageFavoriteSongsUseCase.getFavorites(principal.getName(), pageInfo);

        PagedResponseDto<FavoriteSongDto> responseDto = new PagedResponseDto<>(
                domainPage.getContent().stream().map(favoriteSongMapper::domainToDto).toList(),
                domainPage.getTotalElements(),
                domainPage.getTotalPages(),
                domainPage.getNumber() + 1,
                domainPage.getSize());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{songId}/check")
    public ResponseEntity<Boolean> isFavorite(Principal principal, @PathVariable Long songId) {
        boolean isFav = manageFavoriteSongsUseCase.isFavorite(principal.getName(), songId);
        return ResponseEntity.ok(isFav);
    }
}
