package com.kreitefy.api.home.infrastructure.rest;

import com.kreitefy.api.home.application.services.HomeUserService;
import com.kreitefy.api.home.application.dtos.SongRecommendedDto;
import com.kreitefy.api.home.infrastructure.rest.dtos.response.HomeResponseDto;
import com.kreitefy.api.home.infrastructure.rest.dtos.response.RecomendUserDto;
import com.kreitefy.api.product.application.dtos.SongHomeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class HomeRestController {
    private final HomeUserService homeUserService;

    public HomeRestController(HomeUserService homeUserService) {
        this.homeUserService = homeUserService;
    }

    @GetMapping(value = "/home", produces = "application/json")
    public ResponseEntity<HomeResponseDto> getRecomendadas(Principal principal,
                                                           @RequestParam(required = false) String estilo){
        String username = principal.getName();

        CompletableFuture<List<SongHomeDto>> novedades = CompletableFuture.supplyAsync(() ->
                homeUserService.getLimitAndStyle(5, estilo, "fechaCreacion"));

        CompletableFuture<List<SongHomeDto>> masEscuchadas = CompletableFuture.supplyAsync(() ->
                homeUserService.getLimitAndStyle(5, estilo, "cantRepro"));

        CompletableFuture<RecomendUserDto> recomendadas = CompletableFuture.supplyAsync(() -> {
            SongRecommendedDto recomendacionDominio = homeUserService.getRecomendacion(username);
            return new RecomendUserDto(
                    recomendacionDominio.mensaje(),
                    recomendacionDominio.canciones()
            );
        });

        CompletableFuture.allOf(masEscuchadas, novedades, recomendadas).join();

        return ResponseEntity.ok(new HomeResponseDto(
                masEscuchadas.join(),
                novedades.join(),
                recomendadas.join()
        ));
    }
}
