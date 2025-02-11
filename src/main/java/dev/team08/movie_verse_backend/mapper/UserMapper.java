package dev.team08.movie_verse_backend.mapper;

import dev.team08.movie_verse_backend.dto.request.LoginUserRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterAdminRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterUserRequest;
import dev.team08.movie_verse_backend.entity.Genre;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.repository.GenreRepository;

import java.util.stream.Collectors;
import java.util.*;

public class UserMapper {
    public static User fromRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(registerUserRequest.getEmail());

//        List<String> favoriteGenres = registerUserRequest.getFavoriteGenres() != null ? registerUserRequest.getFavoriteGenres() : new ArrayList<>();
//
//        List<Genre> genres = registerUserRequest.getFavoriteGenres().stream()
//                .map(genreRepository::findByName)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toList());
//        user.setFavoriteGenres(genres);
        return user;
    }



    public static User fromRegisterAdminRequest(RegisterAdminRequest registerAdminRequest) {
        User user = new User();
        user.setUsername(registerAdminRequest.getUsername());
        user.setPassword(registerAdminRequest.getPassword());
        user.setEmail(registerAdminRequest.getEmail());
        return user;
    }

    public static User fromLoginUserRequest(LoginUserRequest loginUserRequest) {
        User user = new User();
        user.setUsername(loginUserRequest.getUsername());
        user.setPassword(loginUserRequest.getPassword());
        return user;
    }


}