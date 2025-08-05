package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {

    boolean genreExists(Set<Genre> genres);

    Genre getGenreById(int genreId);

    Collection<Genre> getAllGenres();

}