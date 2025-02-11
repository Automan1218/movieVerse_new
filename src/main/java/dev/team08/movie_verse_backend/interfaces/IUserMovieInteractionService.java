package dev.team08.movie_verse_backend.interfaces;

import java.util.Optional;
import java.util.UUID;

import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.enums.LikeStatus;

public interface IUserMovieInteractionService {
    void logMovieView(UUID userId, Integer tmdbMovieId);
    void markMovieAsWatched(UUID userId, Integer tmdbMovieId);
    Optional<UserMovieInteraction> getUserMovieInteraction(UUID userId, Integer tmdbMovieId);
	void toggleFavorite(UUID userId, Integer tmdbMovieId);
	void toggleWatchlist(UUID userId, Integer tmdbMovieId);
	void likeOrDislikeMovie(UUID userId, Integer tmdbMovieId, LikeStatus likeStatus);
	void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit);
}
