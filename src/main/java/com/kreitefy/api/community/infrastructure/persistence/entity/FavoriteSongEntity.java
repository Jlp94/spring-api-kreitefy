package com.kreitefy.api.community.infrastructure.persistence.entity;

import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.kreitefy.api.users.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cancion_favorita", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username", "cancion_id"})
})
public class FavoriteSongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cancion_favorita_seq")
    @SequenceGenerator(name = "cancion_favorita_seq", sequenceName = "cancion_favorita_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancion_id", nullable = false)
    private SongEntity song;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public FavoriteSongEntity() {}

    public FavoriteSongEntity(UserEntity user, SongEntity song, LocalDateTime createdAt) {
        this.user = user;
        this.song = song;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public SongEntity getSong() {
        return song;
    }

    public void setSong(SongEntity song) {
        this.song = song;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
