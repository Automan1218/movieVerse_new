package dev.team08.movie_verse_backend.interfaces;

import dev.team08.movie_verse_backend.entity.WatchedMovie;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.Movie;
import java.util.List;

public interface IWatchedMovieService {
    List<WatchedMovie> getUserWatchlist(User user);
    void addMovieToWatchlist(User user, Movie movie);
}
