package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public boolean mpaExists(int mpaId) {
        String queryMpa = "SELECT COUNT(*) FROM rating_mpa WHERE rating_id = ?";
        Integer count = jdbcTemplate.queryForObject(queryMpa, Integer.class, mpaId);
        log.info("MpaDbStorage: проверка существования рейтинга с id: {}", mpaId);
        return count > 0;
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String query = "SELECT * FROM rating_mpa WHERE rating_id = ?";
        try {
            log.info("MpaDbStorage: запрос рейтинга с id: {}", mpaId);
            return jdbcTemplate.queryForObject(query, mpaRowMapper, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("рейтинг с id " + mpaId + " не найден");
        }
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String query = "SELECT * FROM rating_mpa";
        List<Mpa> allMpa = jdbcTemplate.query(query, mpaRowMapper);
        log.info("MpaDbStorage: запрошен список всех рейтингов, всего рейтингов: {}", allMpa.size());
        return allMpa;
    }

}