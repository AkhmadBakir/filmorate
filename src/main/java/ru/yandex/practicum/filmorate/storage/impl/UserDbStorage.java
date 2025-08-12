package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements Storage<User> {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public boolean checkExists(int userId) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        log.info("UserDbStorage: проверка существования пользователя с id: {}", userId);
        return count > 0;
    }

    @Override
    public User findById(int userId) {
        String queryUser = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id " +
                "WHERE u.user_id = ? ";
        if (!checkExists(userId)) {
            throw new NotFoundException("пользователь с id: " + userId + " не найден");
        }
        try {
            log.info("UserDbStorage: запрос пользователя с id: {}", userId);
            List<User> userRows = jdbcTemplate.query(queryUser, userRowMapper, userId);
            User user = userRows.getFirst();
            for (User userRow : userRows) {
                if (userRow.getFriends() != null) {
                    user.getFriends().addAll(userRow.getFriends());
                }
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserDbStorage: не удалось получить пользователя с id: " + userId);
        }
    }

    public void addFriendShips(int userId, int friendId) {
        String queryFriend = "INSERT INTO user_friendships (user_id, friend_id) VALUES (?, ?)";
        if (!checkExists(userId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + userId + " не найден");
        }
        if (!checkExists(friendId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + friendId + " не найден");
        }
        try {
            jdbcTemplate.update(queryFriend, userId, friendId);
            log.info("UserDbStorage: пользователи с id: {} и {} теперь друзья", userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserDbStorage: попытка добавления в друзья пользователю с id: " + userId +
                    " пользователя с id: " + friendId + " не удалась");
        }
    }

    public void deleteFriendShip(int userId, int friendId) {
        String queryDeleteFriend = "DELETE FROM user_friendships WHERE user_id = ? AND friend_id = ?";
        if (!checkExists(userId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + userId + " не найден");
        }
        if (!checkExists(friendId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + friendId + " не найден");
        }
        jdbcTemplate.update(queryDeleteFriend, userId, friendId);
        log.info("UserDbStorage: пользователи с id: {} и {} теперь не друзья", userId, friendId);
    }

    @Override
    public List<User> findAll() {
        String queryUsers = "SELECT u.*, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id";
        List<User> userRows = jdbcTemplate.query(queryUsers, userRowMapper);

        Map<Integer, User> userMap = new HashMap<>();
        for (User user : userRows) {
            int userId = user.getId();
            if (!userMap.containsKey(userId)) {
                userMap.put(userId, user);
            } else {
                User existingUser = userMap.get(userId);
                existingUser.getFriends().addAll(user.getFriends());
            }
        }
        List<User> allUsers = new ArrayList<>(userMap.values());
        log.info("UserDbStorage: количество зарегистрированных пользователей: {}", allUsers.size());
        return allUsers;
    }

    @Override
    public void update(User user) {
        if (!checkExists(user.getId())) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + user.getId() + " не найден");
        }
        try {
            String queryUpdateUser = "UPDATE users SET email = ?, login = ?,  name = ?, birthday = ? WHERE user_id = ?";
            jdbcTemplate.update(queryUpdateUser,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserDbStorage: попытка обновления пользователя с id: " + user.getId() + " не удалась");
        }
    }

    @Override
    public User add(User user) {
        String query = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            user.setId(id);
            log.info("UserDbStorage: добавлен новый пользователь с id: {}", user.getId());
            return user;
        } else {
            throw new RuntimeException("UserDbStorage: не удалось сохранить пользователя: id не сгенерирован");
        }
    }

}