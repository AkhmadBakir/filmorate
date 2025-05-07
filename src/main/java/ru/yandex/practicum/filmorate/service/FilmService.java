package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {

    Film addFilm(Film film);

    List<Film> allFilms();

    Film updateFilm(Film film);

    Film getFilmById(int filmId);

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    Film addDisLike(int filmId, int userId);

    Film removeDisLike(int filmId, int userId);

    Set<Film> getTopFilms(int count);

}
