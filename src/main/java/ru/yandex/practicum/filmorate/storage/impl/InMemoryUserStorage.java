package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.AppValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int identifier = 0;

    @Override
    public User addUser(User user) {
        AppValidator.userValidator(user);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        for (User checkUser : users.values()) {
            if (checkUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException("пользователь с электронной почтой " + user.getEmail() + " уже существует");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++identifier);
        users.put(user.getId(), user);
        log.info("новый пользователь с именем {}, адресом электронной почты {} и id {} добавлен",
                user.getName(), user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("пользователь с id " + user.getId() + " не найден");
        }
        AppValidator.userValidator(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("пользователь с именем {}, адресом электронной почты {} и id {} обновлен",
                user.getName(), user.getEmail(), user.getId());
        return user;
    }

    @Override
    public List<User> allUsers() {
        log.info("количество зарегистрированных пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("пользователь с id " + userId + " не найден");
        }
        log.info("метод public User getUserById(int userId) из класса InMemoryUserStorage");
        return user;
    }

}
