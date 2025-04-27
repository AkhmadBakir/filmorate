package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.AppValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int identifier = 0;

    @Override
    public Film addFilm(Film film) {
        AppValidator.filmValidator(film);
        for (Film checkFilm : films.values()) {
            if (checkFilm.getName().equals(film.getName())) {
                throw new ValidationException("фильм с названием " + film.getName() + " уже существует");
            }
        }
        film.setId(++identifier);
        films.put(film.getId(), film);
        log.info("новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(int filmId, Film film) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("фильм с id " + film.getId() + " не найден");
        }
        AppValidator.filmValidator(film);
        film.setId(filmId);
        films.put(filmId, film);
        log.info("фильм {} с id {} обновлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public List<Film> allFilms() {
        log.info("количество зарегистрированных фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        log.info("запрошен фильм с id " + filmId);
        return film;
    }
}
