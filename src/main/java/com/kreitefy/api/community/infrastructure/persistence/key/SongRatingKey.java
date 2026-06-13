package com.kreitefy.api.community.infrastructure.persistence.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SongRatingKey implements Serializable {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "id_cancion", nullable = false)
    private Long idCancion;

    public SongRatingKey() {}

    public SongRatingKey(String username, Long idCancion) {
        this.username = username;
        this.idCancion = idCancion;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getIdCancion() { return idCancion; }
    public void setIdCancion(Long idCancion) { this.idCancion = idCancion; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SongRatingKey that = (SongRatingKey) o;
        return Objects.equals(username, that.username) && Objects.equals(idCancion, that.idCancion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, idCancion);
    }
}
