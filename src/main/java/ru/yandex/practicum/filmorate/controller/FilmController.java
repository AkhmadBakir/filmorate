package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

/**
 *  Контроллер HTTP запросов FilmController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    /**    POST /films/ — создание фильма.
     *
     * @param film
     * @return
     */
    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        Film newFilm = filmService.addFilm(film);
        log.info("Добавлен новый фильм: {}", newFilm);
        return ResponseEntity.ok(newFilm);
    }

    /**    PUT /films/ — обновление фильма.
     *
     * @param film
     * @return
     */
    @PutMapping()
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        Film updateFilm = filmService.updateFilm(film);
        log.info("Фильм обновлен: {}", film);
        return ResponseEntity.ok(updateFilm);
    }

    /**    GET /films/ — получение всех фильмов.
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Film>> allFilms() {
        log.info("Количество всех фильмов: {}", filmService.allFilms().size());
        return ResponseEntity.ok(filmService.allFilms());
    }

    /**    GET /films/{id} — получение фильма.
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable(value = "id") int id) {
        log.info("Запрошен фильм с id: {}", id);
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.getFilmById(id);
        filmService.addLike(id, userId);
        return ResponseEntity.ok(film);
    }

    /**    DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
     *
     * @param id
     * @param userId
     * @return
     */
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable(value = "id") int id,
                                           @PathVariable(value = "userId") int userId) {
        Film film = filmService.getFilmById(id);
        filmService.removeLike(id, userId);
        log.info("Пользователю с id {} перестал нравится фильм с id: {}", userId, id);
        return ResponseEntity.ok(film);
    }

    /**    PUT /films/{id}/dislike/{userId} — пользователь ставит дизлайк фильму.
     *
     * @param id
     * @param userId
     * @return
     */
    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<Film> addDisLike(@PathVariable(value = "id") int id,
                                           @PathVariable(value = "userId") int userId) {
        Film film = filmService.getFilmById(id);
        filmService.addDisLike(id, userId);
        log.info("Пользователю с id {} не понравился фильм с id: {}", userId, id);
        return ResponseEntity.ok(film);
    }

    /**    DELETE /films/{id}/dislike/{userId} — пользователь удаляет дизлайк.
     *
     * @param id
     * @param userId
     * @return
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<Film> deleteDisLike(@PathVariable(value = "id") int id,
                                              @PathVariable(value = "userId") int userId) {
        Film film = filmService.getFilmById(id);
        filmService.removeDisLike(id, userId);
        log.info("Пользователю с id {} перестал не нравится фильм с id: {}", userId, id);
        return ResponseEntity.ok(film);
    }

    /**    GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count
     * @return
     */
    @GetMapping("/popular")
    public ResponseEntity<Set<Film>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрошен топ {} фильмов", count);
        return ResponseEntity.ok(filmService.getTopFilms(count));
    }

}
