package dev.team08.controller.api;

import dev.team08.movie_verse_backend.controller.api.UserApiController;
import dev.team08.movie_verse_backend.dto.request.GenreRequest;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApiController controller;

    private final String validToken = "Bearer validToken123";
    private final String invalidToken = "Bearer invalid";
    private final String usernameJson = "{\"username\":\"testuser\"}";

    @Test
    void verifyToken_MissingToken_ReturnsUnauthorized() {
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.verifyToken(null, usernameJson));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Authorization token is missing or invalid", exception.getReason());
    }

    @Test
    void verifyToken_InvalidTokenFormat_ReturnsUnauthorized() {
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.verifyToken("InvalidFormat", usernameJson));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void verifyToken_ValidToken_ReturnsOk() {
        // Arrange
        when(userService.verifyToken(validToken, usernameJson)).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.verifyToken(validToken, usernameJson);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).verifyToken(validToken, usernameJson);
    }

    @Test
    void verifyToken_InvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        when(userService.verifyToken(validToken, usernameJson)).thenReturn(false);

        // Act
        ResponseEntity<?> response = controller.verifyToken(validToken, usernameJson);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUserFromToken_ValidToken_ReturnsUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");
        when(userService.getUserFromToken(validToken)).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = controller.getUserFromToken(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void getUserFromToken_InvalidToken_ThrowsException() {
        // Arrange：当传入 invalidToken 时，让 userService 抛出异常
        when(userService.getUserFromToken(invalidToken))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getUserFromToken(invalidToken));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid token", exception.getReason());
    }

    @Test
    void setGenres_Success_ReturnsOk() {
        // Arrange
        List<GenreRequest> genres = List.of(
                new GenreRequest("Action"),
                new GenreRequest("Comedy")
        );
        doNothing().when(userService).setFavoriteGenres(validToken, genres);

        // Act
        ResponseEntity<String> response = controller.setGenres(validToken, genres);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Genres updated successfully", response.getBody());
    }

    @Test
    void setGenres_InvalidToken_ReturnsBadRequest() {
        // Arrange
        List<GenreRequest> genres = Collections.emptyList();
        doThrow(new RuntimeException("Invalid token"))
                .when(userService).setFavoriteGenres(invalidToken, genres);

        // Act
        ResponseEntity<String> response = controller.setGenres(invalidToken, genres);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    void setGenres_NoGenresFound_ReturnsError() {
        // Arrange
        List<GenreRequest> genres = List.of(new GenreRequest("Unknown"));
        doThrow(new RuntimeException("No valid genres found"))
                .when(userService).setFavoriteGenres(validToken, genres);

        // Act
        ResponseEntity<String> response = controller.setGenres(validToken, genres);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No valid genres found", response.getBody());
    }
}
