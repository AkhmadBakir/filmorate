package ru.yandex.practicum.filmorate.util;

import java.time.LocalDate;

public final class AppConstants {

    public static final LocalDate STARTING_DATE = LocalDate.of(1895, 12, 28);
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    private AppConstants() {
        throw new UnsupportedOperationException("Создание экземпляра утилитарного класса AppConstants недопустимо");
    }

}
