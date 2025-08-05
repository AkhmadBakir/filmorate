package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.mappers.UsersExtractor;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean userExists(int userId) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        log.info("UserDbStorage: проверка существования пользователя с id: {}", userId);
        return count > 0;
    }

    @Override
    public User getUserById(int userId) {
        String queryUser = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id " +
                "WHERE u.user_id = ? ";
        if (!userExists(userId)) {
            throw new NotFoundException("пользователь с id: " + userId + " не найден");
        }
        try {
            log.info("UserDbStorage: запрос пользователя с id: {}", userId);
            return jdbcTemplate.query(queryUser, new Object[]{userId}, new UserExtractor());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserDbStorage: не удалось получить пользователя с id: " + userId);
        }
    }

    @Override
    public void addFriendShips(int userId, int friendId) {
        String queryFriend = "INSERT INTO user_friendships (user_id, friend_id) VALUES (?, ?)";
        if (!userExists(userId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + userId + " не найден");
        }
        if (!userExists(friendId)) {
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

    @Override
    public void deleteFriendShip(int userId, int friendId) {
        String queryDeleteFriend = "DELETE FROM user_friendships WHERE user_id = ? AND friend_id = ?";
        if (!userExists(userId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + userId + " не найден");
        }
        if (!userExists(friendId)) {
            throw new NotFoundException("UserDbStorage: пользователь с id: " + friendId + " не найден");
        }
        jdbcTemplate.update(queryDeleteFriend, userId, friendId);
        log.info("UserDbStorage: пользователи с id: {} и {} теперь не друзья", userId, friendId);
    }

    @Override
    public List<User> allUsers() {
        String queryUsers = "SELECT u.*, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id";
        List<User> allUsers = jdbcTemplate.query(queryUsers, new UsersExtractor());
        log.info("UserDbStorage: количество зарегистрированных пользователей: {}", allUsers.size());
        return allUsers;
    }

    @Override
    public void updateUser(User user) {
        if (!userExists(user.getId())) {
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
    public User addUser(User user) {
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