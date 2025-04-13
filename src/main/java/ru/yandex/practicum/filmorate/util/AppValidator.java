package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.util.AppConstants.MAX_DESCRIPTION_LENGTH;
import static ru.yandex.practicum.filmorate.util.AppConstants.STARTING_DATE;

public final class AppValidator {

    public AppValidator() {
        throw new UnsupportedOperationException("Создание экземпляра утилитарного класса AppValidator недопустимо");
    }

    public static void filmValidator(Film film) {
        if ((film.getName() == null || film.getName().isBlank())) {
            throw new ValidationException("название фильма не может быть пустым");
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate().isBefore(STARTING_DATE) || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года, и не может быть позже текущей даты");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }

    public static void userValidator(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }

    }

}
