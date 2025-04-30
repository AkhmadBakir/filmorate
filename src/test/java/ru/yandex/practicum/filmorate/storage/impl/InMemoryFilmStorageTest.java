package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void init() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
    }

    @Test
    void shouldAddNewFilm() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Set<Integer> disLikeUserList1 = new HashSet<>();
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        inMemoryFilmStorage.addFilm(film1);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());
    }

    @Test
    void shouldUpdateFilm() {
        Set<Integer> likeUserList1 = Set.of(1, 2, 3);
        Set<Integer> disLikeUserList1 = Set.of(4, 5, 6);
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        inMemoryFilmStorage.addFilm(film1);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1, 2, 3), Set.of(4, 5, 6));

        Set<Integer> likeUserList2 = Set.of(7, 8, 9);
        Set<Integer> disLikeUserList2 = Set.of(10, 11, 12);
        Film film2 = new Film(1, "name2", "description2", LocalDate.of(2002, 2, 2), 90, likeUserList2, disLikeUserList2);
        inMemoryFilmStorage.updateFilm(1, film2);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name2", "description2", LocalDate.of(2002, 2, 2), 90, Set.of(7, 8, 9), Set.of(10, 11, 12));
    }

    @Test
    void shouldReturnAllFilms() {
        Set<Integer> likeUserList1 = Set.of(1, 2, 3);
        Set<Integer> disLikeUserList1 = Set.of(4, 5, 6);
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        inMemoryFilmStorage.addFilm(film1);
        Set<Integer> likeUserList2 = Set.of(7, 8, 9);
        Set<Integer> disLikeUserList2 = Set.of(10, 11, 12);
        Film film2 = new Film(2, "name2", "description2", LocalDate.of(2002, 2, 2), 120, likeUserList2, disLikeUserList2);
        inMemoryFilmStorage.addFilm(film2);

        assertThat(inMemoryFilmStorage.allFilms())
                .isNotNull()
                .hasSize(2)
                .containsExactly(film1, film2);
    }

    @Test
    void shouldReturnFilmById() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Set<Integer> disLikeUserList1 = new HashSet<>();
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        inMemoryFilmStorage.addFilm(film1);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());
    }

    @Test
    void shouldCheckNullAndReturnValidationException() {
        Film film = null;

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("film не может быть null");
    }

    @Test
    void shouldCheckNameAndReturnValidationException() {
        Film film1 = new Film(1, "", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("название фильма не может быть пустым");

        Film film2 = new Film(2, " ", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film2))
                .isInstanceOf(ValidationException.class)
                .hasMessage("название фильма не может быть пустым");
    }

    @Test
    void shouldCheckMaxDescriptionLengthAndReturnValidationException() {
        Film film1 = new Film(1, "test1", "description1".repeat(200), LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("максимальная длина описания — 200 символов");
    }

    @Test
    void shouldCheckReleaseDateAndReturnValidationException() {
        Film film1 = new Film(1, "test1", "description1", LocalDate.of(1895, 12, 27), 120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("дата релиза — не раньше 28 декабря 1895 года, и не может быть позже текущей даты");

        Film film2 = new Film(2, "test2", "description2", LocalDate.of(3000, 12, 27), 120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film2))
                .isInstanceOf(ValidationException.class)
                .hasMessage("дата релиза — не раньше 28 декабря 1895 года, и не может быть позже текущей даты");
    }

    @Test
    void shouldCheckDurationAndReturnValidationException() {
        Film film1 = new Film(1, "test1", "description1", LocalDate.of(2001, 1, 1), -120, Set.of(), Set.of());

        assertThatThrownBy(() -> inMemoryFilmStorage.addFilm(film1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("продолжительность фильма должна быть положительной");
    }

}