package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServise;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.dto.MpaDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaServise {

    private final MpaStorage mpaStorage;

    @Override
    public MpaDto getMpaById(int mpaId) {
        log.info("MpaServiceImpl: запрошен рейтинг с id {} ", mpaId);
        Mpa mpa = mpaStorage.getMpaById(mpaId);
        return MpaMapper.mapToMpaDto(mpa);
    }

    @Override
    public Collection<MpaDto> getAllMpa() {
        log.info("MpaServiceImpl: запрошен список всех рейтингов, всего рейтингов {}", mpaStorage.getAllMpa().size());
        return mpaStorage.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

}