package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateFilmRequest;

import java.util.*;

/**
 * Контроллер HTTP запросов FilmController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    /**
     * POST /films/ — создание фильма.
     *
     * @param newFilmRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<FilmDto> addFilm(@RequestBody NewFilmRequest newFilmRequest) {
        FilmDto filmDto = filmService.addFilm(newFilmRequest);
        log.info("FilmController: добавлен новый фильм: {}", filmDto.getId());
        return ResponseEntity.ok(filmDto);
    }

    /**
     * PUT /films/ — обновление фильма.
     *
     * @param updateFilmRequest
     * @return
     */
    @PutMapping()
    public ResponseEntity<FilmDto> updateFilm(@RequestBody UpdateFilmRequest updateFilmRequest) {
        FilmDto filmDto = filmService.updateFilm(updateFilmRequest);
        log.info("FilmController: фильм обновлен: {}", updateFilmRequest.getId());
        return ResponseEntity.ok(filmDto);
    }

    /**
     * GET /films/ — получение всех фильмов.
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<FilmDto>> allFilms() {
        log.info("FilmController: количество всех фильмов: {}", filmService.allFilms().size());
        return ResponseEntity.ok(filmService.allFilms());
    }

    /**
     * GET /films/{filmId} — получение фильма.
     *
     * @param filmId
     * @return
     */
    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable(value = "filmId") int filmId) {
        log.info("FilmController: запрошен фильм с id: {}", filmId);
        return ResponseEntity.ok(filmService.getFilmById(filmId));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
        log.info("FilmController: пользователю с id {} понравится фильм с id: {}", userId, filmId);
    }

    /**
     * DELETE /films/{filmId}/like/{userId} — пользователь удаляет лайк.
     *
     * @param filmId
     * @param userId
     * @return
     */
    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable(value = "filmId") int filmId,
                           @PathVariable(value = "userId") int userId) {
        filmService.removeLike(filmId, userId);
        log.info("FilmController: пользователю с id {} перестал нравится фильм с id: {}", userId, filmId);
    }

    /**
     * GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count
     * @return
     */
    @GetMapping("/popular")
    public ResponseEntity<Set<FilmDto>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("FilmController: запрошен топ {} фильмов", count);
        return ResponseEntity.ok(filmService.getTopFilms(count));
    }

}
