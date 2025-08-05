package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.dto.GenreDto;

import java.util.Collection;

public interface GenreServise {

    GenreDto getGenreById(int genreId);

    Collection<GenreDto> getAllGenres();

}