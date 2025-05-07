package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film addFilm(Film film) {
        log.info("новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("фильм {}, с id {} обновлен", film.getName(), film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> allFilms() {
        log.info("запрошен список всех фильмов, всего фильмов {}", filmStorage.allFilms().size());
        return filmStorage.allFilms();
    }

    @Override
    public Film getFilmById(int filmId) {
        log.info("запрошен фильм, с id {} ", filmId);
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getLikeUserList() == null) {
            film.setLikeUserList(new HashSet<>());
        }
        film.getDisLikeUserList().remove(user.getId());
        film.getLikeUserList().add(user.getId());
        log.info("пользователю с id " + userId + " понравился фильм с id " + filmId);
        return film;
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getLikeUserList() == null) {
            film.setLikeUserList(new HashSet<>());
        }
        if (!userStorage.allUsers().contains(user)) {
            throw new NotFoundException("пользователя с id " + userId + " не существует");
        }
        film.getLikeUserList().remove(userId);
        log.info("пользователь с id " + userId + " убрал like с фильма с id " + filmId);
        return film;
    }

    @Override
    public Film addDisLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getDisLikeUserList() == null) {
            film.setDisLikeUserList(new HashSet<>());
        }
        film.getLikeUserList().remove(user.getId());
        film.getDisLikeUserList().add(user.getId());
        log.info("пользователю с id " + userId + " не понравился фильм с id " + filmId);
        return film;
    }

    @Override
    public Film removeDisLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (!userStorage.allUsers().contains(user)) {
            throw new NotFoundException("пользователя с id " + userId + " не существует");
        }
        if (film.getDisLikeUserList() == null) {
            film.setDisLikeUserList(new HashSet<>());
        }
        film.getDisLikeUserList().remove(user.getId());
        log.info("пользователь с id " + userId + " убрал dislike с фильма с id " + filmId);
        return film;
    }

    @Override
    public Set<Film> getTopFilms(int count) {
        log.info("запрос топ-" + count + " фильмов");
        return filmStorage.allFilms().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt((Film film) ->
                        film.getLikeUserList() != null ? film.getLikeUserList().size() : 0).reversed())
                .limit(count)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
