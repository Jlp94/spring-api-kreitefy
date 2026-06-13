package com.kreitefy.api.community.infrastructure.persistence.adapters;

import com.kreitefy.api.community.application.ports.out.UserActivityRepositoryPort;
import com.kreitefy.api.community.domain.models.SongRating;
import com.kreitefy.api.community.infrastructure.mappers.SongRatingMapper;
import com.kreitefy.api.community.infrastructure.persistence.entity.SongHistoryEntity;
import com.kreitefy.api.community.infrastructure.persistence.entity.SongRatingEntity;
import com.kreitefy.api.product.application.ports.out.SongActivityPort;
import com.kreitefy.api.community.infrastructure.persistence.jpa.SongHistoryJpaRepository;
import com.kreitefy.api.community.infrastructure.persistence.jpa.SongRatingJpaRepository;
import com.kreitefy.api.community.infrastructure.persistence.key.SongRatingKey;
import com.kreitefy.api.product.domain.models.Song;
import com.kreitefy.api.product.infrastructure.mappers.SongMapper;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserActivityRepositoryAdapter implements UserActivityRepositoryPort, SongActivityPort {

    private final SongHistoryJpaRepository historialCancionJpaRepository;
    private final SongRatingJpaRepository valoracionCancionJpaRepository;
    private final SongRatingMapper valoracionCancionMapper;
    private final EntityManager entityManager;
    private final SongMapper cancionMapper;

    public UserActivityRepositoryAdapter(
            SongHistoryJpaRepository historialCancionJpaRepository,
            SongRatingJpaRepository valoracionCancionJpaRepository,
            SongRatingMapper valoracionCancionMapper,
            EntityManager entityManager,
            SongMapper cancionMapper) {
        this.historialCancionJpaRepository = historialCancionJpaRepository;
        this.valoracionCancionJpaRepository = valoracionCancionJpaRepository;
        this.valoracionCancionMapper = valoracionCancionMapper;
        this.entityManager = entityManager;
        this.cancionMapper = cancionMapper;
    }

    @Override
    public void registerPlayback(Long cancionId, String username, LocalDateTime now) {
        UserEntity usuarioRef = entityManager.getReference(UserEntity.class, username);
        SongEntity cancionRef = entityManager.getReference(SongEntity.class, cancionId);
        historialCancionJpaRepository.save(
                new SongHistoryEntity(usuarioRef, cancionRef, now)
        );
    }

    @Override
    public void saveRating(Long cancionId, String username, int valoracion) {
        SongRatingKey key = new SongRatingKey(username, cancionId);
        valoracionCancionJpaRepository.findById(key)
                .ifPresentOrElse(
                        entidad -> updateValoracion(valoracion, entidad),
                        () -> createValoracion(cancionId, username, valoracion, key)
                );
    }

    private void updateValoracion(int valoracion, SongRatingEntity entity) {
        entity.setValoracion(valoracion);
        valoracionCancionJpaRepository.save(entity);
    }

    private void createValoracion(Long cancionId, String username, int valoracion, SongRatingKey key) {
        UserEntity usuarioRef = entityManager.getReference(UserEntity.class, username);
        SongEntity cancionRef = entityManager.getReference(SongEntity.class, cancionId);
        valoracionCancionJpaRepository.save(
                new SongRatingEntity(key, valoracion, usuarioRef, cancionRef)
        );
    }

    @Override
    public Optional<SongRating> getRating(Long cancionId, String username) {
        SongRatingKey key = new SongRatingKey(username, cancionId);
        return valoracionCancionJpaRepository.findById(key)
                .map(valoracionCancionMapper::entityToDomain);
    }

    @Override
    public Optional<Integer> getSongRating(Long cancionId, String username) {
        return getRating(cancionId, username).map(v -> v.valoracion());
    }

    @Override
    public List<String> getUserStyles(String username, int limit) {
        return historialCancionJpaRepository
                .findTopEstilosByUsername(username, PageRequest.of(0, limit));
    }

    @Override
    public List<Song> findRecomendaciones(String username, List<String> estilos, int limit) {
        List<SongEntity> entities =
                historialCancionJpaRepository.findRecomendaciones(username, estilos, PageRequest.of(0, limit));
        return cancionMapper.toDomainListFromEntity(entities);
    }
}