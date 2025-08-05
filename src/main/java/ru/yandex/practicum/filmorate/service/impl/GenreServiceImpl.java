package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServise;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreServise {

    private final GenreStorage genreStorage;

    @Override
    public GenreDto getGenreById(int genreId) {
        log.info("GenreServiceImpl: запрошен жанр с id {} ", genreId);
        Genre genre = genreStorage.getGenreById(genreId);
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public Collection<GenreDto> getAllGenres() {
        log.info("GenreServiceImpl: запрошен список всех жанров, всего жанров {}", genreStorage.getAllGenres().size());
        return genreStorage.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

}