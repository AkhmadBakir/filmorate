package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Services;
import ru.yandex.practicum.filmorate.storage.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements Services<User> {

    private final UserDbStorage userDbStorage;

    @Override
    public void add(User user) {
        userDbStorage.add(user);
        log.info("UserServiceImpl: добавлен пользователь с id {} ", user.getId());
    }

    @Override
    public void update(User user) {
        userDbStorage.update(user);
        log.info("UserServiceImpl: данные пользователя с id {} обновлены ", user.getId());
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userDbStorage.findAll();
        log.info("UserServiceImpl: запрошен список всех пользователей, всего пользователей {}", allUsers.size());
        return allUsers;
    }

    @Override
    public User findById(int userId) {
        log.info("UserServiceImpl: запрошен пользователь с id {} ", userId);
        return userDbStorage.findById(userId);
    }

    public void addFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("UserServiceImpl: попытка добавления пользователя к себе в друзья");
        }
        userDbStorage.addFriendShips(userId, friendId);
        log.info("UserServiceImpl: пользователи с id " + userId + " и " + friendId + " теперь друзья");
    }

    public void removeFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("UserServiceImpl: попытка добавления пользователя к себе в друзья");
        }
        userDbStorage.deleteFriendShip(userId, friendId);
        log.info("UserServiceImpl: пользователи с id " + userId + " и " + friendId + "больше не друзья");
    }

    public List<UserDto> getFriendsList(int userId) {
        User user = userDbStorage.findById(userId);
        List<Integer> friendsId = new ArrayList<>(user.getFriends());
        log.info("UserServiceImpl: запрошен список друзей пользователя " + userId + " всего их " + friendsId.size());
        return friendsId.stream()
                .map(userDbStorage::findById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getCommonFriendsList(int userId, int otherId) {
        User user = userDbStorage.findById(userId);
        User otherUser = userDbStorage.findById(otherId);
        Set<Integer> friendsList = new HashSet<>(user.getFriends());
        friendsList.retainAll(otherUser.getFriends());
        List<Integer> commonFriendsList = new ArrayList<>(friendsList);
        log.info("UserServiceImpl: запрошен список общих друзей пользователей с id " + userId + " и " + otherId);
        return commonFriendsList.stream()
                .map(userDbStorage::findById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

}