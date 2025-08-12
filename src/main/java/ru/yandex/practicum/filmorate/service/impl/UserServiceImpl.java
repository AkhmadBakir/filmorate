package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Services;
import ru.yandex.practicum.filmorate.storage.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.util.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements Services<UserDto, NewUserRequest, UpdateUserRequest> {

    private final UserDbStorage userDbStorage;

    @Override
    public UserDto add(NewUserRequest newUserRequest) {
        UserValidator.validator(newUserRequest);
        User user = userDbStorage.add(UserMapper.mapToUser(newUserRequest));
        log.info("UserServiceImpl: добавлен пользователь с id {} ", user.getId());
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UpdateUserRequest updateUserRequest) {
        User user = userDbStorage.findById(updateUserRequest.getId());
        if (user == null) {
            throw new NotFoundException("пользователь с id " + updateUserRequest.getId() + " не найден");
        }
        UserMapper.updateUser(user, updateUserRequest);
        userDbStorage.update(user);
        log.info("UserServiceImpl: данные пользователя с id {} обновлены ", user.getId());
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> allUsers = userDbStorage.findAll();
        log.info("UserServiceImpl: запрошен список всех пользователей, всего пользователей {}", allUsers.size());
        return allUsers.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(int userId) {
        log.info("UserServiceImpl: запрошен пользователь с id {} ", userId);
        User user = userDbStorage.findById(userId);
        return UserMapper.mapToUserDto(user);
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