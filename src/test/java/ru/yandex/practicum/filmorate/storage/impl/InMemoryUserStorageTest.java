package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {

    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void init() {
        inMemoryUserStorage = new InMemoryUserStorage();
    }

    @Test
    void shouldAddUser() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
        inMemoryUserStorage.addUser(user);

        assertThat(inMemoryUserStorage.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void shouldUpdateUser() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        inMemoryUserStorage.addUser(user1);

        assertThat(inMemoryUserStorage.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);

        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        inMemoryUserStorage.updateUser(1, user2);

        assertThat(inMemoryUserStorage.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
    }

    @Test
    void shouldReturnAllUsers() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList1);
        inMemoryUserStorage.addUser(user1);
        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
        inMemoryUserStorage.addUser(user2);

        assertThat(inMemoryUserStorage.allUsers())
                .isNotNull()
                .hasSize(2)
                .containsExactly(user1, user2);

    }

    @Test
    void shouldGetUserById() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
        inMemoryUserStorage.addUser(user);

        assertThat(inMemoryUserStorage.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void shouldCheckNameAndSetLoginIfNullOrBlank() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "login1", null, LocalDate.of(2001, 1, 1), friendsList1);
        inMemoryUserStorage.addUser(user1);

        assertThat(inMemoryUserStorage.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(1, "test1@test.ru", "login1", "login1", LocalDate.of(2001, 1, 1), friendsList1);

        Set<Integer> friendsList2 = Set.of(1, 2, 3);
        User user2 = new User(2, "test2@test.ru", "login2", " ", LocalDate.of(2001, 1, 1), friendsList2);
        inMemoryUserStorage.addUser(user2);

        assertThat(inMemoryUserStorage.getUserById(2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriends)
                .containsExactly(2, "test2@test.ru", "login2", "login2", LocalDate.of(2001, 1, 1), friendsList2);
    }

    @Test
    void shouldCheckNullAndReturnValidationException() {
        User user = null;

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("user не может быть null");
    }

    @Test
    void shouldCheckLoginAndReturnValidationException() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, "test1@test.ru", "", "name1", LocalDate.of(2001, 1, 1), friendsList1);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("логин не может быть пустым и содержать пробелы");

        Set<Integer> friendsList2 = Set.of(1, 2, 3);
        User user2 = new User(2, "test2@test.ru", " ", "name2", LocalDate.of(2002, 2, 2), friendsList2);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user2))
                .isInstanceOf(ValidationException.class)
                .hasMessage("логин не может быть пустым и содержать пробелы");
    }

    @Test
    void shouldCheckEmailAndReturnValidationException() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User(1, null, "test1", "name1", LocalDate.of(2001, 1, 1), friendsList1);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("электронная почта не может быть пустой и должна содержать символ @");

        Set<Integer> friendsList2 = Set.of(1, 2, 3);
        User user2 = new User(2, " ", "test2", "name2", LocalDate.of(2002, 2, 2), friendsList2);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user2))
                .isInstanceOf(ValidationException.class)
                .hasMessage("электронная почта не может быть пустой и должна содержать символ @");

        Set<Integer> friendsList3 = Set.of(1, 2, 3);
        User user3 = new User(3, "test3test.ru", "test3", "name3", LocalDate.of(2003, 3, 3), friendsList3);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user3))
                .isInstanceOf(ValidationException.class)
                .hasMessage("электронная почта не может быть пустой и должна содержать символ @");
    }

    @Test
    void shouldCheckDateOfBirthdayAndReturnValidationException() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user = new User(1, "test@test.ru", "test", "name", LocalDate.of(3001, 1, 1), friendsList1);

        assertThatThrownBy(() -> inMemoryUserStorage.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("дата рождения не может быть в будущем");
    }

}