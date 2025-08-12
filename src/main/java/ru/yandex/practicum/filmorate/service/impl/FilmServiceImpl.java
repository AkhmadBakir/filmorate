package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.util.FilmValidator;
import ru.yandex.practicum.filmorate.service.Services;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements Services<FilmDto, NewFilmRequest, UpdateFilmRequest> {

    private final FilmDbStorage filmDBStorage;

    @Override
    public FilmDto add(NewFilmRequest newFilmRequest) {
        FilmValidator.validator(newFilmRequest);
        Film film = filmDBStorage.add(FilmMapper.mapToFilm(newFilmRequest));
        log.info("FilmServiceImpl: новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(filmDBStorage.add(film));
    }

    @Override
    public FilmDto update(UpdateFilmRequest updateFilmRequest) {
        Film film = filmDBStorage.findById(updateFilmRequest.getId());
        FilmMapper.updateFilm(film, updateFilmRequest);
        filmDBStorage.update(film);
        log.info("FilmServiceImpl: фильм {}, с id {} обновлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> findAll() {
        log.info("FilmServiceImpl: запрошен список всех фильмов, всего фильмов {}", filmDBStorage.findAll().size());
        return filmDBStorage.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto findById(int filmId) {
        log.info("FilmServiceImpl: запрошен фильм, с id {} ", filmId);
        Film film = filmDBStorage.findById(filmId);
        return FilmMapper.mapToFilmDto(film);
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