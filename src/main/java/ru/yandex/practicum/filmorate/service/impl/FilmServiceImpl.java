package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.util.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public FilmDto addFilm(NewFilmRequest newFilmRequest) {
        FilmValidator.validator(newFilmRequest);
        Film film = filmStorage.addFilm(FilmMapper.mapToFilm(newFilmRequest));
        log.info("FilmServiceImpl: новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(filmStorage.addFilm(film));
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest updateFilmRequest) {
        Film film = filmStorage.getFilmById(updateFilmRequest.getId());
        FilmMapper.updateFilm(film, updateFilmRequest);
        filmStorage.updateFilm(film);
        log.info("FilmServiceImpl: фильм {}, с id {} обновлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> allFilms() {
        log.info("FilmServiceImpl: запрошен список всех фильмов, всего фильмов {}", filmStorage.allFilms().size());
        return filmStorage.allFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto getFilmById(int filmId) {
        log.info("FilmServiceImpl: запрошен фильм, с id {} ", filmId);
        Film film = filmStorage.getFilmById(filmId);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} нравится фильм с id: {}", userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} перестал нравится фильм с id: {}", userId, filmId);
    }

    @Override
    public Set<FilmDto> getTopFilms(int count) {
        log.info("FilmServiceImpl: запрос топ-" + count + " фильмов");
        List<Film> topPopular = filmStorage.getTopPopular(count);
        return topPopular.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}