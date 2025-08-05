package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    boolean filmExists(int filmId);

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    void updateFilm(Film film);

    List<Film> allFilms();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getTopPopular(int count);

}
