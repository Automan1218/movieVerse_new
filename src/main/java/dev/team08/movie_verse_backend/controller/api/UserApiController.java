package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.dto.request.GenreRequest;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody String usernameJson) {
        // Check if the token is present
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }

        // Verify the token
        boolean isValid = userService.verifyToken(token, usernameJson);

        // Return a 200 OK response if the token is valid, otherwise return a 401 Unauthorized response
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/token")
    public ResponseEntity<User> getUserFromToken(@RequestHeader(value = "Authorization", required = false) String token) {
        // Check if the token is present
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }

        // Get the user from the JWT token
        User user = userService.getUserFromToken(token);

        // Return the user and a 200 OK response
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/setGenres")
    public ResponseEntity<String> setGenres(
            @RequestHeader("Authorization") String token,
            @RequestBody List<GenreRequest> genreRequests) {
        try {
            userService.setFavoriteGenres(token, genreRequests);
            return ResponseEntity.ok("Genres updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/user_interactions_table")
//    public ResponseEntity<List<Map<String, Object>>> getUserInteractionsTable(@RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(userService.getUserInteractions(token));
//    }

}
