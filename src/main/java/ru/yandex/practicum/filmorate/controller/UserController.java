package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.AppValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int identifier = 0;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        AppValidator.userValidator(user);
        for (User checkUser : users.values()) {
            if (checkUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с электронной почтой " + user.getEmail() + " уже существует");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++identifier);
        users.put(user.getId(), user);
        log.info("Новый пользователь с именем {}, адресом электронной почты {} и id {} добавлен",
                user.getName(), user.getEmail(), user.getId());
        return ResponseEntity.ok(users.get(user.getId()));
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("пользователь с id " + user.getId() + "не найден");
        }
        AppValidator.userValidator(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с именем {}, адресом электронной почты {} и id {} обновлен",
                user.getName(), user.getEmail(), user.getId());
        return ResponseEntity.ok(users.get(user.getId()));
    }

    @GetMapping()
    public List<User> allUsers() {
        log.info("Количество зарегистрированных пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

}
