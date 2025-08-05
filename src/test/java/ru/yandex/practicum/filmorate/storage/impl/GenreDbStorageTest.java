package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class, GenreRowMapper.class})
class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    void shouldGenreExists() {
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(10, "Comedy");
        boolean isExists1 = genreDbStorage.genreExists(Set.of(genre1));
        boolean isExists2 = genreDbStorage.genreExists(Set.of(genre2));

        assertThat(isExists1)
                .isEqualTo(true);
        assertThat(isExists2)
                .isEqualTo(false);
    }

    @Test
    void shouldGetGenreById() {
        Genre genre1 = genreDbStorage.getGenreById(1);
        Genre genre2 = new Genre(1, "Комедия");

        assertThat(genre1)
                .isEqualTo(genre2);
    }

    @Test
    void shouldGetAllGenres() {
        Collection<Genre> allGenres = genreDbStorage.getAllGenres();

        AssertionsForInterfaceTypes.assertThat(allGenres)
                .isNotNull()
                .hasSize(6);
    }
}