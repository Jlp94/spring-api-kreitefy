package com.kreitefy.api.product.infrastructure.persistence.entity;

import com.kreitefy.api.product.domain.models.Song;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CANCION")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cancion_seq")
    @SequenceGenerator(name = "cancion_seq", sequenceName = "cancion_seq", allocationSize = 50)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "cantRepro")
    private Integer cantRepro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_album")
    private AlbumEntity album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estilo")
    private StyleEntity estiloMusical;

    @Column(name = "fechaCreacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @Version
    private Integer version;

    public SongEntity() {}

    public SongEntity(Song cancion, AlbumEntity album, StyleEntity estiloMusical) {
        this.titulo = cancion.titulo();
        this.duracion = cancion.duracion();
        this.cantRepro = cancion.cantRepro();
        this.album = album;
        this.estiloMusical = estiloMusical;
        this.version = cancion.version();
    }

    public void updateFromDomain(Song cancion, AlbumEntity album, StyleEntity estiloMusical) {
        this.titulo = cancion.titulo();
        this.duracion = cancion.duracion();
        this.cantRepro = cancion.cantRepro();
        this.album = album;
        this.estiloMusical = estiloMusical;
        this.version = cancion.version();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getDuracion() {
        return duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
    public Integer getCantRepro() {
        return cantRepro;
    }
    public void setCantRepro(Integer cantRepro) {
        this.cantRepro = cantRepro;
    }
    public AlbumEntity getAlbum() {
        return album;
    }
    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }
    public StyleEntity getEstiloMusical() {
    return estiloMusical;
    }
    public void setEstiloMusical(StyleEntity estiloMusical) {
        this.estiloMusical = estiloMusical;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
