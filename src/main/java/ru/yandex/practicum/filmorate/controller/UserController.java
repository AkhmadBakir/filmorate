package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //    POST /users/ — создание пользователя.
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        User newUser = userService.addUser(user);
        log.info("Добавлен новый пользователь: {}", newUser.getId());
        return ResponseEntity.ok(newUser);
    }

    //    PUT /users/ — обновление пользователя.
    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Данные пользователя обновлены: {}", user.getId());
        User updateUser = userService.updateUser(user.getId(), user);
        return ResponseEntity.ok(updateUser);
    }

    //    GET /users/ — получение всех пользователей.
    @GetMapping()
    public ResponseEntity<List<User>> allUsers() {
        log.info("Количество всех пользователей: {}", userService.allUsers().size());
        return ResponseEntity.ok(userService.allUsers());
    }

    //    GET /users/{id} — получение пользователя.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("Пользователя с id " + id + " не существует");
        }
        log.info("Запрошен пользователь с id: {}", id);
        return ResponseEntity.ok(user);
    }

    //    PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriends(@PathVariable(value = "id") int id,
                                           @PathVariable(value = "friendId") int friendId) {
        if (id == friendId) {
            throw new ValidationException("попытка добавления пользователя к себе в друзья");
        }
        User user = userService.addFriends(id, friendId);
        log.info("Пользователи с id {} и id {} добавлены в друзья", id, friendId);
        return ResponseEntity.ok(user);
    }

    //    DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable(value = "id") int id,
                                             @PathVariable(value = "friendId") int friendId) {
        User user = userService.removeFriends(id, friendId);
        log.info("Пользователи с id {} и id {} удалены из друзей", id, friendId);
        return ResponseEntity.ok(user);
    }

    //    GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable(value = "id") int id) {
        log.info("Запрошен пользователь с id: {}", id);
        return ResponseEntity.ok(userService.getFriendsList(id));
    }

    //    GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriendsList(@PathVariable(value = "id") int id,
                                                           @PathVariable(value = "otherId") int otherId) {
        log.info("Запрошен список общих друзей пользователей с id {} и id {}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriendsList(id, otherId));
    }

}
