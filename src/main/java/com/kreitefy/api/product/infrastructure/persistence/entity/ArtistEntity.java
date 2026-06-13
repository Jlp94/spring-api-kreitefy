package com.kreitefy.api.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "ARTISTA")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artista_seq")
    @SequenceGenerator(name = "artista_seq", sequenceName = "artista_seq",  allocationSize = 1)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "artista")
    private Set<AlbumEntity> albums;

    @Version
    private Integer version;

    public ArtistEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<AlbumEntity> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<AlbumEntity> albums) {
        this.albums = albums;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
