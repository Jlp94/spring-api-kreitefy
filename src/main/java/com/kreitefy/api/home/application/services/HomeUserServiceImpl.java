package com.kreitefy.api.home.application.services;

import com.kreitefy.api.community.application.ports.out.UserActivityRepositoryPort;

import com.kreitefy.api.product.application.ports.out.HomeCatalogPort;
import com.kreitefy.api.product.application.mappers.SongHomeMapper;
import com.kreitefy.api.product.application.dtos.SongHomeDto;

import com.kreitefy.api.home.application.dtos.SongRecommendedDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HomeUserServiceImpl implements HomeUserService {

    private final HomeCatalogPort homeCatalogPort;
    private final UserActivityRepositoryPort userActivityRepositoryPort;
    private final SongHomeMapper cancionTiraMapper;

    public HomeUserServiceImpl(HomeCatalogPort homeCatalogPort,
                               UserActivityRepositoryPort userActivityRepositoryPort,
                               SongHomeMapper cancionTiraMapper) {
        this.homeCatalogPort = homeCatalogPort;
        this.userActivityRepositoryPort = userActivityRepositoryPort;
        this.cancionTiraMapper = cancionTiraMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongHomeDto> getLimitAndStyle(int limit, String estilo, String filtro) {
        return cancionTiraMapper.toDtoList(homeCatalogPort.findLimitFilter(limit, estilo, filtro));
    }

    @Override
    @Transactional(readOnly = true)
    public SongRecommendedDto getRecomendacion(String username) {
        List<String> estilos = userActivityRepositoryPort.getUserStyles(username, 2);
        if (estilos == null || estilos.isEmpty()) {
            return new SongRecommendedDto("¡Empieza a escuchar música para recibir recomendaciones!", List.of());
        }
        String mensaje = "Últimamente escuchas más " + String.join(" y ", estilos);
        List<SongHomeDto> canciones = cancionTiraMapper.toDtoList(
                userActivityRepositoryPort.findRecomendaciones(username, estilos, 5));
        return new SongRecommendedDto(mensaje, canciones);
    }
}
