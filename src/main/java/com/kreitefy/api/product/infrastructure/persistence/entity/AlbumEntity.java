package com.kreitefy.api.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "album")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_seq")
    @SequenceGenerator(name = "album_seq", sequenceName = "album_seq", allocationSize = 50)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artista")
    private ArtistEntity artista;

    @OneToMany(mappedBy = "album")
    private Set<SongEntity> canciones;

    @Version
    private Integer version;

    public AlbumEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Set<SongEntity> getCanciones() {
        return canciones;
    }

    public void setCanciones(Set<SongEntity> canciones) {
        this.canciones = canciones;
    }

    public ArtistEntity getArtista() {
        return artista;
    }

    public void setArtista(ArtistEntity artista) {
        this.artista = artista;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
