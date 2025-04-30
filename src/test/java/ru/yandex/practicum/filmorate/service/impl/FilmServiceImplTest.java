package ru.yandex.practicum.filmorate.service.impl;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FilmServiceImplTest {

    private FilmServiceImpl filmServiceImpl;
    private InMemoryFilmStorage inMemoryFilmStorage;
    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void init() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        inMemoryUserStorage = new InMemoryUserStorage();
        filmServiceImpl = new FilmServiceImpl(inMemoryFilmStorage, inMemoryUserStorage);
    }

    @Test
    void shouldAddFilm() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Set<Integer> disLikeUserList1 = new HashSet<>();
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        filmServiceImpl.addFilm(film1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());
    }

    @Test
    void shouldUpdateFilm() {
        Set<Integer> likeUserList1 = Set.of(1, 2, 3);
        Set<Integer> disLikeUserList1 = Set.of(4, 5, 6);
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        filmServiceImpl.addFilm(film1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1, 2, 3), Set.of(4, 5, 6));

        Set<Integer> likeUserList2 = Set.of(7, 8, 9);
        Set<Integer> disLikeUserList2 = Set.of(10, 11, 12);
        Film film2 = new Film(1, "name2", "description2", LocalDate.of(2002, 2, 2), 90, likeUserList2, disLikeUserList2);
        filmServiceImpl.updateFilm(1, film2);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name2", "description2", LocalDate.of(2002, 2, 2), 90, Set.of(7, 8, 9), Set.of(10, 11, 12));
    }

    @Test
    void shouldReturnAllFilms() {
        Set<Integer> likeUserList1 = Set.of(1, 2, 3);
        Set<Integer> disLikeUserList1 = Set.of(4, 5, 6);
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        filmServiceImpl.addFilm(film1);
        Set<Integer> likeUserList2 = Set.of(7, 8, 9);
        Set<Integer> disLikeUserList2 = Set.of(10, 11, 12);
        Film film2 = new Film(2, "name2", "description2", LocalDate.of(2002, 2, 2), 120, likeUserList2, disLikeUserList2);
        filmServiceImpl.addFilm(film2);

        AssertionsForInterfaceTypes.assertThat(filmServiceImpl.allFilms())
                .isNotNull()
                .hasSize(2)
                .containsExactly(film1, film2);
    }

    @Test
    void shouldGetFilmById() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Set<Integer> disLikeUserList1 = new HashSet<>();
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList1, disLikeUserList1);
        filmServiceImpl.addFilm(film1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());
    }

    @Test
    void shouldAddLikeAndDisLike() {
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        User user2 = new User(2, "test21@test.ru", "login2", "name2", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        inMemoryUserStorage.addUser(user1);
        inMemoryUserStorage.addUser(user2);
        Set<Integer> likeUserList = new HashSet<>();
        Set<Integer> disLikeUserList = new HashSet<>();
        Film film = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList, disLikeUserList);
        inMemoryFilmStorage.addFilm(film);
        filmServiceImpl.addLike(1, 1);
        filmServiceImpl.addDisLike(1, 2);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1), Set.of(2));
    }

    @Test
    void shouldAddDisLikeAddDeleteLike() {
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        inMemoryUserStorage.addUser(user1);
        Set<Integer> likeUserList = new HashSet<>();
        Set<Integer> disLikeUserList = new HashSet<>();
        Film film = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList, disLikeUserList);
        inMemoryFilmStorage.addFilm(film);
        filmServiceImpl.addLike(1, 1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1), Set.of());

        filmServiceImpl.addDisLike(1, 1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of(1));
    }

    @Test
    void shouldAddLikeAddDeleteDisLike() {
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        inMemoryUserStorage.addUser(user1);
        Set<Integer> likeUserList = new HashSet<>();
        Set<Integer> disLikeUserList = new HashSet<>();
        Film film = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList, disLikeUserList);
        inMemoryFilmStorage.addFilm(film);
        filmServiceImpl.addDisLike(1, 1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of(1));

        filmServiceImpl.addLike(1, 1);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1), Set.of());
    }

    @Test
    void shouldRemoveLikeAndDisLike() {
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        User user2 = new User(2, "test21@test.ru", "login2", "name2", LocalDate.of(2001, 1, 1), Set.of(1, 2, 3));
        inMemoryUserStorage.addUser(user1);
        inMemoryUserStorage.addUser(user2);
        Set<Integer> likeUserList = new HashSet<>();
        likeUserList.add(1);
        Set<Integer> disLikeUserList = new HashSet<>();
        disLikeUserList.add(2);
        Film film = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, likeUserList, disLikeUserList);
        inMemoryFilmStorage.addFilm(film);
        filmServiceImpl.removeLike(1, 1);
        filmServiceImpl.removeDisLike(1, 2);

        assertThat(filmServiceImpl.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList, Film::getDisLikeUserList)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(), Set.of());
    }

    @Test
    void getTopFilms() {
        Film film1 = new Film(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(1, 2, 3), Set.of());
        filmServiceImpl.addFilm(film1);
        Film film2 = new Film(2, "name2", "description2", LocalDate.of(2002, 2, 2), 120, Set.of(4, 5, 6, 7), Set.of());
        filmServiceImpl.addFilm(film2);

        AssertionsForInterfaceTypes.assertThat(filmServiceImpl.getTopFilms(2))
                .isNotNull()
                .hasSize(2)
                .containsExactly(film2, film1);
    }

}