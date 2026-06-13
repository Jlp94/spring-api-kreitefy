package com.kreitefy.api.community.infrastructure.persistence.entity;

import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORIAL_CANCION")
public class SongHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_cancion_seq")
    @SequenceGenerator(name = "historial_cancion_seq", sequenceName = "historial_cancion_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private UserEntity username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cancion")
    private SongEntity cancion;

    @Column(name = "fecha_reproduccion")
    private LocalDateTime fechaReproduccion;

    public SongHistoryEntity() {}

    public SongHistoryEntity(UserEntity username, SongEntity cancion, LocalDateTime fechaReproduccion) {
        this.username = username;
        this.cancion = cancion;
        this.fechaReproduccion = fechaReproduccion;
    }

    public Long getId() {
    return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UserEntity getUsername() {
        return username;
    }
    public void setUsername(UserEntity username) {
        this.username = username;
    }
    public SongEntity getCancion() {
    return cancion;
    }
    public void setCancion(SongEntity cancion) {
        this.cancion = cancion;
    }
    public LocalDateTime getFechaReproduccion() {
        return fechaReproduccion;
    }
    public void setFechaReproduccion(LocalDateTime fechaReproduccion) {
    this.fechaReproduccion = fechaReproduccion;
}}
