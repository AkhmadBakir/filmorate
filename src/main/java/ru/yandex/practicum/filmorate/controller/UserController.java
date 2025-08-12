package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UserDto;

import java.util.List;

/**
 * Контроллер HTTP запросов UserController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceIml;

    /**
     * POST /users/ — создание пользователя.
     *
     * @param newUserRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody NewUserRequest newUserRequest) {
        UserDto userDto = userServiceIml.add(newUserRequest);
        log.info("UserController: добавлен новый пользователь: {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    /**
     * PUT /users/ — обновление пользователя.
     *
     * @param updateUserRequest
     * @return
     */
    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UserDto userDto = userServiceIml.update(updateUserRequest);
        log.info("UserController: данные пользователя обновлены: {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    /**
     * GET /users/ — получение всех пользователей.
     *
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<UserDto>> allUsers() {
        log.info("UserController: количество всех пользователей: {}", userServiceIml.findAll().size());
        return ResponseEntity.ok(userServiceIml.findAll());
    }

    /**
     * GET /users/{userId} — получение пользователя.
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable(value = "userId") int userId) {
        log.info("UserController: запрошен пользователь с id: {}", userId);
        return ResponseEntity.ok(userServiceIml.findById(userId));
    }

    /**
     * PUT /users/{id}/friends/{friendId} — добавление в друзья.
     *
     * @param userId
     * @param friendId
     * @return
     */
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriends(@PathVariable(value = "userId") int userId,
                           @PathVariable(value = "friendId") int friendId) {
        userServiceIml.addFriends(userId, friendId);
        log.info("UserController: пользователи с id {} и id {} добавлены в друзья", userId, friendId);
    }

    /**
     * DELETE /users/{id}/friends/{friendId} — удаление из друзей.
     *
     * @param userId
     * @param friendId
     * @return
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable(value = "userId") int userId,
                             @PathVariable(value = "friendId") int friendId) {
        userServiceIml.removeFriends(userId, friendId);
        log.info("UserController: пользователи с id {} и id {} удалены из друзей", userId, friendId);
    }

    /**
     * GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UserDto>> getFriendsList(@PathVariable(value = "userId") int userId) {
        log.info("UserController: запрошен пользователь с id: {}", userId);
        return ResponseEntity.ok(userServiceIml.getFriendsList(userId));
    }

    /**
     * GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
     *
     * @param userId
     * @param otherId
     * @return
     */
    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriendsList(@PathVariable(value = "userId") int userId,
                                                              @PathVariable(value = "otherId") int otherId) {
        log.info("UserController: запрошен список общих друзей пользователей с id {} и id {}", userId, otherId);
        return ResponseEntity.ok(userServiceIml.getCommonFriendsList(userId, otherId));
    }

}
