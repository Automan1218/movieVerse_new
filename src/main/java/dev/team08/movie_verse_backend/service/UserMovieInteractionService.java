package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.entity.MovieReview;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.enums.WatchStatus;
import dev.team08.movie_verse_backend.repository.MovieReviewRepository;
import dev.team08.movie_verse_backend.repository.UserMovieInteractionRepository;
import dev.team08.movie_verse_backend.repository.UserRepository;
import dev.team08.movie_verse_backend.interfaces.IUserMovieInteractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserMovieInteractionService implements IUserMovieInteractionService {

    private final UserMovieInteractionRepository userMovieInteractionRepository;
    private final MovieReviewRepository movieReviewRepository;
    private final UserRepository userRepository;

    public UserMovieInteractionService(UserMovieInteractionRepository userMovieInteractionRepository, 
    		UserRepository userRepository, MovieReviewRepository movieReviewRepository
    		) 
    {
        this.userMovieInteractionRepository = userMovieInteractionRepository;
        this.userRepository = userRepository;
        this.movieReviewRepository = movieReviewRepository;	
    }
    /**
     * Ensures that an interaction entry exists for the given user and movie.
     */
    private UserMovieInteraction getOrCreateInteraction(UUID userId, Integer tmdbMovieId) {
        return userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId, tmdbMovieId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    UserMovieInteraction newInteraction = new UserMovieInteraction(user, tmdbMovieId);
                    return userMovieInteractionRepository.save(newInteraction);
                });
    }

    @Override
    @Transactional
    public void logMovieView(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setViews(interaction.getViews() + 1);  // ✅ Increment views
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void markMovieAsWatched(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setWatchStatus(WatchStatus.WATCHED);
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void likeOrDislikeMovie(UUID userId, Integer tmdbMovieId, LikeStatus likeStatus) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setLikeStatus(likeStatus);
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void toggleFavorite(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setFavorite(!interaction.isFavorite());
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void toggleWatchlist(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setWatchStatus(
                interaction.getWatchStatus() == WatchStatus.PLANNED? WatchStatus.NO_PLANS : WatchStatus.PLANNED
        );
        userMovieInteractionRepository.save(interaction);
    }
    @Override
    @Transactional
    public void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        MovieReview review = interaction.getReview();

        if (review == null) {
            review = new MovieReview(interaction, reviewText);
            interaction.setReview(review);
        } else {
            if (isEdit) {
                review.editReview(reviewText);  // ✅ Track edited version
            } else {
                review.setOriginalReviewText(reviewText);
            }
        }

        movieReviewRepository.save(review);
        userMovieInteractionRepository.save(interaction);
    }
    
	@Override
	public Optional<UserMovieInteraction> getUserMovieInteraction(UUID userId, Integer tmdbMovieId) {
		return userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId,tmdbMovieId);
	}

}
