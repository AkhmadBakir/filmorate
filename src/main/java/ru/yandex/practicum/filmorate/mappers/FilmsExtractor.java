package ru.yandex.practicum.filmorate.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmsExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Integer, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            int filmId = rs.getInt("film_id");

            Film film = filmMap.get(filmId);
            if (film == null) {
                film = Film.builder()
                        .id(rs.getInt("film_id"))
                        .name(rs.getString("film_name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .likeUserList(new HashSet<>())
                        .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                        .genres(new HashSet<>())
                        .build();
                filmMap.put(filmId, film);
            }

            Integer userId = rs.getInt("like_user_id");
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));

            if (!rs.wasNull()) {
                film.getLikeUserList().add(userId);
                film.getGenres().add(genre);
            }

        }

        return new ArrayList<>(filmMap.values());
    }

}