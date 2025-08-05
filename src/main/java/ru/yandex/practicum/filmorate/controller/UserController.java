package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
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

    private final UserService userService;

    /**
     * POST /users/ — создание пользователя.
     *
     * @param newUserRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody NewUserRequest newUserRequest) {
        UserDto userDto = userService.addUser(newUserRequest);
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
        UserDto userDto = userService.updateUser(updateUserRequest);
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
        log.info("UserController: количество всех пользователей: {}", userService.allUsers().size());
        return ResponseEntity.ok(userService.allUsers());
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
        return ResponseEntity.ok(userService.getUserById(userId));
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
        userService.addFriends(userId, friendId);
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
        userService.removeFriends(userId, friendId);
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
        return ResponseEntity.ok(userService.getFriendsList(userId));
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
        return ResponseEntity.ok(userService.getCommonFriendsList(userId, otherId));
    }

}
