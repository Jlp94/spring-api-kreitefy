package com.kreitefy.api.community.application.services;

import com.kreitefy.api.community.application.ports.out.FavoriteSongRepositoryPort;
import com.kreitefy.api.community.domain.models.FavoriteSong;
import com.kreitefy.api.shared.domain.models.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteSongsServiceImplTest {

    @Mock
    private FavoriteSongRepositoryPort favoriteSongRepositoryPort;

    @InjectMocks
    private FavoriteSongsServiceImpl favoriteSongsService;

    @Test
    @DisplayName("Should successfully add a song to favorites when it does not exist")
    void shouldAddFavoriteWhenNotExists() {
        String username = "jose";
        Long songId = 42L;

        when(favoriteSongRepositoryPort.exists(username, songId)).thenReturn(false);
        when(favoriteSongRepositoryPort.save(any(FavoriteSong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteSong result = favoriteSongsService.addFavorite(username, songId);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals(songId, result.songId());
        verify(favoriteSongRepositoryPort, times(1)).exists(username, songId);
        verify(favoriteSongRepositoryPort, times(1)).save(any(FavoriteSong.class));
    }

    @Test
    @DisplayName("Should not duplicate saving a song to favorites if it is already a favorite")
    void shouldNotDuplicateFavoriteWhenAlreadyExists() {
        String username = "jose";
        Long songId = 42L;

        when(favoriteSongRepositoryPort.exists(username, songId)).thenReturn(true);

        FavoriteSong result = favoriteSongsService.addFavorite(username, songId);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals(songId, result.songId());
        verify(favoriteSongRepositoryPort, times(1)).exists(username, songId);
        verify(favoriteSongRepositoryPort, never()).save(any(FavoriteSong.class));
    }

    @Test
    @DisplayName("Should successfully remove a song from favorites")
    void shouldRemoveFavorite() {
        String username = "jose";
        Long songId = 42L;

        doNothing().when(favoriteSongRepositoryPort).delete(username, songId);

        favoriteSongsService.removeFavorite(username, songId);

        verify(favoriteSongRepositoryPort, times(1)).delete(username, songId);
    }

    @Test
    @DisplayName("Should fetch paginated favorites for a user")
    void shouldGetFavorites() {
        String username = "jose";
        Optional<PageInfo> pageInfo = PageInfo.of(1, 10);
        Page<FavoriteSong> page = new PageImpl<>(Collections.emptyList());

        when(favoriteSongRepositoryPort.findByUser(username, pageInfo)).thenReturn(page);

        Page<FavoriteSong> result = favoriteSongsService.getFavorites(username, pageInfo);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(favoriteSongRepositoryPort, times(1)).findByUser(username, pageInfo);
    }

    @Test
    @DisplayName("Should check if a song is favorite and return true")
    void shouldReturnTrueIfFavoriteExists() {
        String username = "jose";
        Long songId = 42L;

        when(favoriteSongRepositoryPort.exists(username, songId)).thenReturn(true);

        boolean result = favoriteSongsService.isFavorite(username, songId);

        assertTrue(result);
        verify(favoriteSongRepositoryPort, times(1)).exists(username, songId);
    }

    @Test
    @DisplayName("Should check if a song is favorite and return false")
    void shouldReturnFalseIfFavoriteDoesNotExist() {
        String username = "jose";
        Long songId = 42L;

        when(favoriteSongRepositoryPort.exists(username, songId)).thenReturn(false);

        boolean result = favoriteSongsService.isFavorite(username, songId);

        assertFalse(result);
        verify(favoriteSongRepositoryPort, times(1)).exists(username, songId);
    }
}
