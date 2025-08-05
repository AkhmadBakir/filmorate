package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaRowMapper.class})
class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    void shouldMpaExists() {
        boolean isExists1 = mpaDbStorage.mpaExists(1);
        boolean isExists2 = mpaDbStorage.mpaExists(100);

        assertThat(isExists1)
                .isEqualTo(true);
        assertThat(isExists2)
                .isEqualTo(false);
    }

    @Test
    void shouldGetMpaById() {
        Mpa mpa1 = mpaDbStorage.getMpaById(1);
        Mpa mpa2 = new Mpa(1, "G");

        assertThat(mpa1)
                .isEqualTo(mpa2);
    }

    @Test
    void shouldGetAllMpa() {
        Collection<Mpa> allMpa = mpaDbStorage.getAllMpa();

        AssertionsForInterfaceTypes.assertThat(allMpa)
                .isNotNull()
                .hasSize(5);
    }
}