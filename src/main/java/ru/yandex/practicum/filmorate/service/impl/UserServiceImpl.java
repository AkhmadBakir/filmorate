package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        log.info("добавлен пользователь с id {} ", user.getId());
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("данные пользователя с id {} обновлены ", user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> allUsers() {
        log.info("запрошен список всех пользователей, всего пользователей {}", userStorage.allUsers().size());
        return userStorage.allUsers();
    }

    @Override
    public User getUserById(int userId) {
        log.info("запрошен пользователь с id {} ", userId);
        return userStorage.getUserById(userId);
    }

    @Override
    public User addFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("попытка добавления пользователя к себе в друзья");
        }
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friendUser.getFriends() == null) {
            friendUser.setFriends(new HashSet<>());
        }
        user.getFriends().add(friendUser.getId());
        friendUser.getFriends().add(user.getId());
        log.info("пользователи с id " + userId + " и " + friendId + "теперь друзья");
        return user;
    }

    @Override
    public User removeFriends(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friendUser.getFriends() == null) {
            friendUser.setFriends(new HashSet<>());
        }
        user.getFriends().remove(friendUser.getId());
        friendUser.getFriends().remove(user.getId());
        log.info("пользователи с id " + userId + " и " + friendId + "больше не друзья");
        return user;
    }

    @Override
    public List<User> getFriendsList(int userId) {
        User user = userStorage.getUserById(userId);
        List<Integer> friendsId = new ArrayList<>(user.getFriends());
        log.info("запрошен список друзей пользователя " + userId + " всего их " + friendsId.size());
        return friendsId.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriendsList(int userId, int otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        Set<Integer> friendsList = new HashSet<>(user.getFriends());
        friendsList.retainAll(otherUser.getFriends());
        List<Integer> commonFriendsList = new ArrayList<>(friendsList);
        log.info("запрошен список общих друзей пользователей с id " + userId + " и " + otherId);
        return commonFriendsList.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

}
