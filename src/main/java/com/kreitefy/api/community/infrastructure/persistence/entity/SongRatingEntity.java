package com.kreitefy.api.community.infrastructure.persistence.entity;


import com.kreitefy.api.community.infrastructure.persistence.key.SongRatingKey;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
@Table(name = "VALORACION_CANCION")
public class SongRatingEntity {
    @EmbeddedId
    private SongRatingKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCancion")
    @JoinColumn(name= "id_cancion")
    private SongEntity cancion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("username")
    @JoinColumn(name= "username")
    private UserEntity usuario;

    @Column(name = "valoracion",columnDefinition = "INTEGER DEFAULT 0")
    @Min(0)
    @Max(4)
    private Integer valoracion;

    public SongRatingEntity() {}

    public SongRatingEntity(SongEntity cancion, UserEntity usuario, Integer valoracion) {
        this(new SongRatingKey(usuario.getUsername(), cancion.getId()), valoracion, usuario, cancion);
    }

    public SongRatingEntity(SongRatingKey id, Integer valoracion, UserEntity usuario, SongEntity cancion) {
        this.id = id;
        this.valoracion = valoracion;
        this.usuario = usuario;
        this.cancion = cancion;
    }

    public SongRatingKey getId() {
        return id;
    }
    public void setId(SongRatingKey id) {
        this.id = id;
    }
    public Integer getValoracion() {
        return valoracion;
    }
    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }
    public SongEntity getCancion() {
        return cancion;
    }
    public void setCancion(SongEntity cancion) {
        this.cancion = cancion;
    }
    public UserEntity getUsuario() {
        return usuario;
    }
    public void setUsuario(UserEntity usuario) {
        this.usuario = usuario;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass() || id == null) return false;
        SongRatingEntity that = (SongRatingEntity) o;
        return id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
