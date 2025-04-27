package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User updateUser(int userId, User user);

    List<User> allUsers();

    User getUserById(int userId);

    User addFriends(int userId, int friendId);

    User removeFriends(int userId, int friendId);

    List<User> getFriendsList(int userId);

    List<User> getCommonFriendsList(int userId, int otherId);

}
