package com.kreitefy.api.community.infrastructure.rest;

import com.kreitefy.api.community.application.dtos.HistoryDetailDto;
import com.kreitefy.api.shared.application.dtos.PagedResponseDto;
import com.kreitefy.api.community.application.ports.in.GetAllHistoryUseCase;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
public class SongHistoryRestController {
    private final GetAllHistoryUseCase historialPerfilUseCase;

    public SongHistoryRestController(GetAllHistoryUseCase historialPerfilUseCase) {
        this.historialPerfilUseCase = historialPerfilUseCase;
    }

    @GetMapping(value = "/users/me/history", produces = "application/json")
    public ResponseEntity<PagedResponseDto<HistoryDetailDto>> getUserHistory(
            Principal principal,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Optional<PageInfo> pageInfo = PageInfo.of(page, size);

        PagedResponseDto<HistoryDetailDto> response = historialPerfilUseCase
        .getUserHistory(principal.getName(), pageInfo);

        return ResponseEntity.ok(response);
    }

}
