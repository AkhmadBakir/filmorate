package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getInt("user_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getObject("birthday", LocalDate.class))
                .friends(new HashSet<>())
                .build();

        Integer friendId = rs.getObject("friend_id", Integer.class);
        if (friendId != null) {
            user.getFriends().add(friendId);
        }

        return user;
    }
}
