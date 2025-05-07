package ru.yandex.practicum.filmorate.service.impl;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        userServiceImpl = new UserServiceImpl(inMemoryUserStorage);
    }

    @Test
    void shouldAddUser() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
        userServiceImpl.addUser(user);

        assertThat(userServiceImpl.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void shouldUpdateUser() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);

        assertThat(userServiceImpl.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);

        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.updateUser(user2);

        assertThat(userServiceImpl.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
    }

    @Test
    void shouldReturnAllUsers() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);
        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.addUser(user2);

        AssertionsForInterfaceTypes.assertThat(userServiceImpl.allUsers())
                .isNotNull()
                .hasSize(2)
                .containsExactly(user1, user2);

    }

    @Test
    void shouldGetUserById() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
        userServiceImpl.addUser(user);

        assertThat(userServiceImpl.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void addFriends() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);
        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.addUser(user2);

        assertThat(userServiceImpl.addFriends(1, 2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of(2));

        assertThat(userServiceImpl.getUserById(2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), Set.of(1));
    }

    @Test
    void removeFriends() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);
        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.addUser(user2);
        userServiceImpl.addFriends(1, 2);

        assertThat(userServiceImpl.removeFriends(1, 2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of());

        assertThat(userServiceImpl.getUserById(2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), Set.of());
    }

    @Test
    void getFriendsList() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);
        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.addUser(user2);
        userServiceImpl.addFriends(1, 2);

        assertThat(userServiceImpl.getFriendsList(1))
                .isNotNull()
                .isEqualTo(List.of(user2));
    }

    @Test
    void getCommonFriendsList() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        userServiceImpl.addUser(user1);
        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        userServiceImpl.addUser(user2);
        Set<Integer> friendsList3 = new HashSet<>();
        User user3 = new User(3, "test3@test.ru", "login3", "name3", LocalDate.of(2003, 3, 3), friendsList3);
        userServiceImpl.addUser(user3);
        Set<Integer> friendsList4 = new HashSet<>();
        User user4 = new User(4, "test4@test.ru", "login4", "name4", LocalDate.of(2004, 4, 4), friendsList4);
        userServiceImpl.addUser(user4);
        userServiceImpl.addFriends(1, 3);
        userServiceImpl.addFriends(1, 4);
        userServiceImpl.addFriends(2, 3);
        userServiceImpl.addFriends(2, 4);

        assertThat(userServiceImpl.getCommonFriendsList(1, 2))
                .isNotNull()
                .isEqualTo(List.of(user3, user4));
    }
}