package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements Storage<Film> {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmRowMapper filmRowMapper;

    @Override
    public boolean checkExists(int filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM films WHERE film_id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        log.info("FilmDbStorage: проверка существования фильма с id: {}", filmId);
        return count > 0;
    }

    @Override
    public Film findById(int filmId) {
        String queryFilm = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = ?;";
        if (!checkExists(filmId)) {
            throw new NotFoundException("фильм с id: " + filmId + " не найден");
        }
        try {
            log.info("FilmDbStorage: запрос фильма с id: {}", filmId);
            List<Film> filmRows = jdbcTemplate.query(queryFilm, filmRowMapper, filmId);

            Film film = filmRows.getFirst();

            for (Film filmRow : filmRows) {
                if (filmRow.getLikeUserList() != null) {
                    film.getLikeUserList().addAll(filmRow.getLikeUserList());
                }
                if (filmRow.getGenres() != null) {
                    film.getGenres().addAll(filmRow.getGenres());
                }
            }
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("FilmDbStorage: фильм с id: " + filmId + " не найден");
        }
    }

    @Override
    public List<Film> findAll() {
        String queryFilms = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id;";
        List<Film> filmRows = jdbcTemplate.query(queryFilms, filmRowMapper);

        Map<Integer, Film> filmMap = new HashMap<>();
        for (Film film : filmRows) {
            int filmId = film.getId();
            if (!filmMap.containsKey(filmId)) {
                filmMap.put(filmId, film);
            } else {
                Film existingFilm = filmMap.get(filmId);
                existingFilm.getLikeUserList().addAll(film.getLikeUserList());
                existingFilm.getGenres().addAll(film.getGenres());
            }
        }

        List<Film> allFilms = new ArrayList<>(filmMap.values());
        log.info("FilmDbStorage: запрошен список всех фильмов, количество зарегистрированных фильмов: {}", allFilms.size());

        return allFilms;
    }

    @Override
    public void update(Film film) {
        try {
            String queryUpdateFilm = "UPDATE films SET name = ?, description = ?,  release_date = ?, duration = ? WHERE film_id = ?";
            jdbcTemplate.update(queryUpdateFilm,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId());
            log.info("FilmDbStorage: фильм с id: {} обновлен", film.getId());

            jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId());
                }
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("FilmDbStorage: фильм с id: " + film.getId() + " не найден");
        }
    }

    private void addGenresToFilm(int filmId, Set<Genre> genres) {
        String queryAddGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(queryAddGenres, filmId, genre.getId());
            log.info("FilmDbStorage: к фильму с id: {} добавлен жанр с id: {}", filmId, genre.getId());
        }
    }

    @Override
    public Film add(Film film) {
        String queryAddFilm = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        Mpa mpa = film.getMpa();
        if (mpa == null || !mpaDbStorage.checkExists(mpa.getId())) {
            throw new NotFoundException("рейтинг с id " + (mpa != null ? mpa.getId() : "null") + " не существует");
        }

        Set<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty() && !genreDbStorage.checkExists(genres)) {
            throw new NotFoundException("FilmDbStorage: один или несколько жанров не существуют");
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(queryAddFilm, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, mpa.getId());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id == null) {
            throw new RuntimeException("FilmDbStorage: не удалось сохранить фильм: id не сгенерирован");
        }

        film.setId(id);

        if (genres != null && !genres.isEmpty()) {
            addGenresToFilm(id, genres);
        }
        log.info("FilmDbStorage: добавлен новый фильм с id: {}", film.getId());
        return film;
    }

    public void addLike(int filmId, int userId) {
        String queryAddLike = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(queryAddLike, filmId, userId);
        log.info("FilmDbStorage: к фильму с id: {} добавлен like от пользователя с id: {}", filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        String queryRemoveLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(queryRemoveLike, filmId, userId);
        log.info("FilmDbStorage: у фильма с id: {} удален like от пользователя с id: {}", filmId, userId);
    }

    public List<Film> getTopPopular(int count) {
        String queryTopPopularFilms = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id;";
        List<Film> filmRows = jdbcTemplate.query(queryTopPopularFilms, filmRowMapper);

        Map<Integer, Film> filmMap = new HashMap<>();
        for (Film film : filmRows) {
            int filmId = film.getId();
            if (!filmMap.containsKey(filmId)) {
                filmMap.put(filmId, film);
            } else {
                Film existingFilm = filmMap.get(filmId);
                existingFilm.getLikeUserList().addAll(film.getLikeUserList());
                existingFilm.getGenres().addAll(film.getGenres());
            }
        }

        List<Film> allFilms = new ArrayList<>(filmMap.values());
        log.info("FilmDbStorage: запрошен топ-{} список фильмов", count);
        return allFilms.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt((Film film) ->
                        film.getLikeUserList() != null ? film.getLikeUserList().size() : 0).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}