package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.AppValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int identifier = 0;

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        AppValidator.filmValidator(film);
        film.setId(++identifier);
        films.put(film.getId(), film);
        log.info("Новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return ResponseEntity.ok(films.get(film.getId()));
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("фильм с id " + film.getId() + " не найден");
        }
        AppValidator.filmValidator(film);
        film.setId(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} с id {} обновлен", film.getName(), film.getId());
        return ResponseEntity.ok(films.get(film.getId()));
    }

    @GetMapping
    public List<Film> allFilms() {
        log.info("Количество зарегистрированных фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

}
