package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                .likeUserList(new HashSet<>())
                .genres(new HashSet<>())
                .build();

        Integer userId = rs.getObject("like_user_id", Integer.class);
        if (userId != null) {
            film.getLikeUserList().add(userId);
        }

        Integer genreId = rs.getObject("genre_id", Integer.class);
        if (genreId != null) {
            String genreName = rs.getString("genre_name");
            film.getGenres().add(new Genre(genreId, genreName));
        }

        return film;
    }

}
