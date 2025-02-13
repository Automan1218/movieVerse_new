package dev.team08.controller.api;

import dev.team08.movie_verse_backend.controller.api.UserMovieInteractionApiController;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.interfaces.IUserMovieInteractionService;
import dev.team08.movie_verse_backend.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMovieInteractionApiControllerTest {

    @Mock
    private IUserMovieInteractionService userMovieInteractionService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserMovieInteractionApiController controller;

    private final UUID userId = UUID.randomUUID();
    private final String validToken = "Bearer validToken";
    private final Integer tmdbMovieId = 123;

    private User dummyUser;

    @BeforeEach
    void setUp() {
        // 构造一个模拟的 User 对象，并设置 id
        dummyUser = new User();
        dummyUser.setId(userId);
        // 当调用 userService.getUserFromToken 时，返回 dummyUser
        when(userService.getUserFromToken(validToken)).thenReturn(dummyUser);
    }

    @Test
    void logMovieView_ReturnsSuccess() {
        ResponseEntity<String> response = controller.logMovieView(validToken, tmdbMovieId);
        verify(userMovieInteractionService).logMovieView(userId, tmdbMovieId);
        assertEquals("Movie view logged successfully.", response.getBody());
    }

    @Test
    void markMovieAsWatched_ReturnsSuccess() {
        ResponseEntity<String> response = controller.markMovieAsWatched(validToken, tmdbMovieId);
        verify(userMovieInteractionService).markMovieAsWatched(userId, tmdbMovieId);
        assertEquals("Movie marked as watched.", response.getBody());
    }

    @Test
    void likeOrDislikeMovie_ReturnsSuccess() {
        // 假设 LikeStatus 枚举中存在一个值，例如 LIKE
        LikeStatus likeStatus = LikeStatus.LIKE;
        ResponseEntity<String> response = controller.likeOrDislikeMovie(validToken, tmdbMovieId, likeStatus);
        verify(userMovieInteractionService).likeOrDislikeMovie(userId, tmdbMovieId, likeStatus);
        assertEquals("Movie interaction updated.", response.getBody());
    }

    @Test
    void toggleFavorite_ReturnsSuccess() {
        ResponseEntity<String> response = controller.toggleFavorite(validToken, tmdbMovieId);
        verify(userMovieInteractionService).toggleFavorite(userId, tmdbMovieId);
        assertEquals("Favorite status updated.", response.getBody());
    }

    @Test
    void toggleWatchlist_ReturnsSuccess() {
        ResponseEntity<String> response = controller.toggleWatchlist(validToken, tmdbMovieId);
        verify(userMovieInteractionService).toggleWatchlist(userId, tmdbMovieId);
        assertEquals("Watchlist status updated.", response.getBody());
    }

    @Test
    void getUserMovieInteraction_ReturnsInteraction() {
        UserMovieInteraction dummyInteraction = new UserMovieInteraction();
        // 根据需要可设置 dummyInteraction 的其他属性
        when(userMovieInteractionService.getUserMovieInteraction(userId, tmdbMovieId))
                .thenReturn(Optional.of(dummyInteraction));

        ResponseEntity<Optional<UserMovieInteraction>> response = controller.getUserMovieInteraction(validToken, tmdbMovieId);
        verify(userMovieInteractionService).getUserMovieInteraction(userId, tmdbMovieId);
        assertTrue(response.getBody().isPresent());
        assertEquals(dummyInteraction, response.getBody().get());
    }
}
