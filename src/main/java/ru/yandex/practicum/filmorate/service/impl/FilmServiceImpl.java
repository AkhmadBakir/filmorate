package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.service.Services;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements Services<Film> {

    private final FilmDbStorage filmDBStorage;

    @Override
    public void add(Film film) {
        filmDBStorage.add(film);
        log.info("FilmServiceImpl: новый фильм {}, с id {} добавлен", film.getName(), film.getId());
    }

    @Override
    public void update(Film film) {
        filmDBStorage.update(film);
        log.info("FilmServiceImpl: фильм {}, с id {} обновлен", film.getName(), film.getId());
    }

    @Override
    public List<Film> findAll() {
        List<Film> allFilms = filmDBStorage.findAll();
        log.info("FilmServiceImpl: запрошен список всех фильмов, всего фильмов {}", allFilms.size());
        return allFilms;
    }

    @Override
    public Film findById(int filmId) {
        log.info("FilmServiceImpl: запрошен фильм, с id {} ", filmId);
        return filmDBStorage.findById(filmId);
    }

    public void addLike(int filmId, int userId) {
        filmDBStorage.addLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} нравится фильм с id: {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        filmDBStorage.removeLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} перестал нравится фильм с id: {}", userId, filmId);
    }

    public Set<FilmDto> getTopFilms(int count) {
        log.info("FilmServiceImpl: запрос топ-" + count + " фильмов");
        List<Film> topPopular = filmDBStorage.getTopPopular(count);
        return topPopular.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}