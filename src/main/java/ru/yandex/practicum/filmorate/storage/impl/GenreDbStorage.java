package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public boolean checkExists(Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return true;
        }

        String sql = "SELECT genre_id FROM genres WHERE genre_id IN (:ids)";
        List<Integer> inputIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", inputIds);

        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Integer> existingIds = namedTemplate.queryForList(sql, params, Integer.class);
        log.info("GenreDbStorage: проверка существования жанра");
        return existingIds.size() == inputIds.size();
    }

    @Override
    public Genre getGenreById(int genreId) {
        String query = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            log.info("GenreDbStorage: запрос жанра с id: {}", genreId);
            return jdbcTemplate.queryForObject(query, genreRowMapper, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("GenreDbStorage: жанр с id " + genreId + " не найден");
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String query = "SELECT * FROM genres";
        List<Genre> allGenres = jdbcTemplate.query(query, genreRowMapper);
        log.info("GenreDbStorage: запрошен список всех жанров, всего жанров: {}", allGenres.size());
        return allGenres;

    }

}