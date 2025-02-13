package dev.team08.movie_verse_backend.api;

import dev.team08.movie_verse_backend.controller.api.MovieRatingApiController;
import dev.team08.movie_verse_backend.entity.MovieRating;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.interfaces.IMovieRatingService;
import dev.team08.movie_verse_backend.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MovieRatingApiControllerTest {

    @Mock
    private IMovieRatingService movieRatingService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private MovieRatingApiController controller;

    private final UUID testUserId = UUID.randomUUID();
    private final String validToken = "validToken";
    private final Integer tmdbMovieId = 123;
    private final Double testRating = 4.5;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        // 使用 lenient() 标记此存根，避免未使用的 stubbing 异常，同时保证 getId() 能返回 testUserId
        lenient().when(mockUser.getId()).thenReturn(testUserId);
    }

    @Test
    void rateMovie_ValidRequest_ReturnsSuccess() {
        when(userService.getUserFromToken("Bearer " + validToken)).thenReturn(mockUser);

        ResponseEntity<String> response = controller.rateMovie(
                "Bearer " + validToken,
                tmdbMovieId,
                testRating
        );

        verify(movieRatingService).addOrUpdateRating(testUserId, tmdbMovieId, testRating);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rating submitted successfully.", response.getBody());
    }

    @Test
    void deleteRating_ValidRequest_ReturnsSuccess() {
        when(userService.getUserFromToken("Bearer " + validToken)).thenReturn(mockUser);

        ResponseEntity<String> response = controller.deleteRating(
                "Bearer " + validToken,
                tmdbMovieId
        );

        verify(movieRatingService).deleteRating(testUserId, tmdbMovieId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rating deleted successfully.", response.getBody());
    }

    @Test
    void getUserRating_RatingExists_ReturnsRating() {
        when(userService.getUserFromToken("Bearer " + validToken)).thenReturn(mockUser);
        MovieRating expectedRating = new MovieRating();
        when(movieRatingService.getUserRating(testUserId, tmdbMovieId))
                .thenReturn(Optional.of(expectedRating));

        ResponseEntity<?> response = controller.getUserRating(
                "Bearer " + validToken,
                tmdbMovieId
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(expectedRating), response.getBody());
    }

    @Test
    void getUserRating_RatingMissing_ReturnsNotFound() {
        when(userService.getUserFromToken("Bearer " + validToken)).thenReturn(mockUser);
        when(movieRatingService.getUserRating(testUserId, tmdbMovieId))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getUserRating(
                "Bearer " + validToken,
                tmdbMovieId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAverageRating_ValidRequest_ReturnsValue() {
        Double expectedAverage = 4.5;
        when(movieRatingService.getAverageMovieRating(tmdbMovieId))
                .thenReturn(expectedAverage);

        ResponseEntity<Double> response = controller.getAverageRating(tmdbMovieId);

        verify(movieRatingService).getAverageMovieRating(tmdbMovieId); // 验证存根调用
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAverage, response.getBody());
    }
}
