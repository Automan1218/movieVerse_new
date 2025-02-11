package dev.team08.movie_verse_backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")

public class User extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String email;
    private String avatar;
    private String bio;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchedMovie> watchedMovies;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_genre",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<Genre> favoriteGenres;

    public List<Genre> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<Genre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<WatchedMovie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<WatchedMovie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }
}
