package com.kreitefy.api.community.infrastructure.persistence.jpa;

import com.kreitefy.api.community.infrastructure.persistence.entity.SongHistoryEntity;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongHistoryJpaRepository extends JpaRepository<SongHistoryEntity, Long> {
    @Query("""
    SELECT c.estiloMusical.estilo 
    FROM SongHistoryEntity h 
    JOIN h.cancion c 
    WHERE h.username.username = :username 
    GROUP BY c.estiloMusical.estilo 
    ORDER BY COUNT(h) DESC
    """)
    List<String> findTopEstilosByUsername(@Param("username") String username, Pageable pageable);

    @Query("""
        SELECT c
        FROM SongHistoryEntity h
        JOIN h.cancion c
        JOIN SongRatingEntity r ON r.cancion.id = c.id
        WHERE h.username.username = :username
          AND c.estiloMusical.estilo IN :estilos
        GROUP BY c.id, c.titulo, c.duracion, c.cantRepro, c.album, c.estiloMusical, c.fechaCreacion
        HAVING AVG(r.valoracion) >= 3
        ORDER BY COUNT(h) DESC
    """)
    List<SongEntity> findRecomendaciones(
            @Param("username") String username,
            @Param("estilos") List<String> estilos,
            Pageable pageable);

    Page<SongHistoryEntity> findByUsername_Username(String username, Pageable pageable);
}
