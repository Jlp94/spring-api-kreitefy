package com.kreitefy.api.product.infrastructure.persistence.querys;

import com.kreitefy.api.product.domain.criteria.SongCriteria;
import com.kreitefy.api.product.infrastructure.persistence.entity.SongEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.kreitefy.api.product.infrastructure.persistence.entity.QSongEntity.songEntity;
import static com.kreitefy.api.product.infrastructure.persistence.entity.QAlbumEntity.albumEntity;
import static com.kreitefy.api.product.infrastructure.persistence.entity.QArtistEntity.artistEntity;
import static com.kreitefy.api.product.infrastructure.persistence.entity.QStyleEntity.styleEntity;

public class SongRepositoryCustomImpl implements SongRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<SongEntity> findByCriteriaQueryDsl(SongCriteria criteria, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.titulo() != null && !criteria.titulo().isBlank()) {
            builder.and(songEntity.titulo.toLowerCase().like("%" + criteria.titulo().toLowerCase() + "%"));
        }
        if (criteria.nombreAlbum() != null && !criteria.nombreAlbum().isBlank()) {
            builder.and(albumEntity.nombre.toLowerCase().like("%" + criteria.nombreAlbum().toLowerCase() + "%"));
        }
        if (criteria.nombreArtista() != null && !criteria.nombreArtista().isBlank()) {
            builder.and(artistEntity.nombre.toLowerCase().like("%" + criteria.nombreArtista().toLowerCase() + "%"));
        }
        if (criteria.estilo() != null && !criteria.estilo().isBlank()) {
            builder.and(styleEntity.estilo.eq(criteria.estilo()));
        }

        Long total = Optional.ofNullable(
                queryFactory
                        .select(songEntity.count())
                        .from(songEntity)
                        .join(songEntity.album, albumEntity)
                        .join(albumEntity.artista, artistEntity)
                        .leftJoin(songEntity.estiloMusical, styleEntity)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        List<SongEntity> resultList = queryFactory
                .selectFrom(songEntity)
                .leftJoin(songEntity.album, albumEntity).fetchJoin()
                .leftJoin(albumEntity.artista, artistEntity).fetchJoin()
                .leftJoin(songEntity.estiloMusical, styleEntity).fetchJoin()
                .where(builder)
                .orderBy(songEntity.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(resultList, pageable, total);
    }
}