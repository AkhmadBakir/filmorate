package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("test@test.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2001, 1, 1))
                .friends(new HashSet<>())
                .build();
    }

    @Test
    void shouldAddUserAndReturnUser() throws Exception {
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.birthday").value(user.getBirthday().toString()))
                .andExpect(jsonPath("$.friends").isArray());
    }

    @Test
    void shouldUpdateAndReturnUser() throws Exception {
        User updateUser = User.builder()
                .id(1)
                .email("test1@test.ru")
                .login("test1")
                .name("test1")
                .birthday(LocalDate.of(2002, 2, 2))
                .friends(new HashSet<>())
                .build();

        Mockito.when(userService.updateUser(Mockito.eq(1), Mockito.any(User.class))).thenReturn(updateUser);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateUser.getId()))
                .andExpect(jsonPath("$.email").value(updateUser.getEmail()))
                .andExpect(jsonPath("$.login").value(updateUser.getLogin()))
                .andExpect(jsonPath("$.name").value(updateUser.getName()))
                .andExpect(jsonPath("$.birthday").value(updateUser.getBirthday().toString()))
                .andExpect(jsonPath("$.friends").isArray());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        User user1 = User.builder()
                .id(1)
                .email("test1@test.ru")
                .login("test1")
                .name("test1")
                .birthday(LocalDate.of(2001, 1, 1))
                .friends(new HashSet<>())
                .build();
        User user2 = User.builder()
                .id(2)
                .email("test2@test.ru")
                .login("test2")
                .name("test2")
                .birthday(LocalDate.of(2002, 2, 2))
                .friends(new HashSet<>())
                .build();

        List<User> allUsersList = List.of(user1, user2);

        Mockito.when(userService.allUsers()).thenReturn(allUsersList);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allUsersList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allUsersList.size()))
                .andExpect(jsonPath("$[0].id").value(allUsersList.get(0).getId()))
                .andExpect(jsonPath("$[0].email").value(allUsersList.get(0).getEmail()))
                .andExpect(jsonPath("$[0].login").value(allUsersList.get(0).getLogin()))
                .andExpect(jsonPath("$[0].name").value(allUsersList.get(0).getName()))
                .andExpect(jsonPath("$[0].birthday").value(allUsersList.get(0).getBirthday().toString()))
                .andExpect(jsonPath("$[0].friends").isArray())
                .andExpect(jsonPath("$[1].id").value(allUsersList.get(1).getId()))
                .andExpect(jsonPath("$[1].email").value(allUsersList.get(1).getEmail()))
                .andExpect(jsonPath("$[1].login").value(allUsersList.get(1).getLogin()))
                .andExpect(jsonPath("$[1].name").value(allUsersList.get(1).getName()))
                .andExpect(jsonPath("$[1].birthday").value(allUsersList.get(1).getBirthday().toString()))
                .andExpect(jsonPath("$[1].friends").isArray());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.birthday").value(user.getBirthday().toString()))
                .andExpect(jsonPath("$.friends").isArray());
    }

    @Test
    void shouldAddFriendsAndReturnUser() throws Exception {
        User user1 = User.builder()
                .id(1)
                .email("test1@test.ru")
                .login("test1")
                .name("test1")
                .birthday(LocalDate.of(2001, 1, 1))
                .friends(Set.of(2))
                .build();

        Mockito.when(userService.addFriends(1, 2)).thenReturn(user1);

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test1@test.ru"))
                .andExpect(jsonPath("$.login").value("test1"))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.birthday").value("2001-01-01"))
                .andExpect(jsonPath("$.friends").isArray())
                .andExpect(jsonPath("$.friends[0]").value(2));
    }

    @Test
    void shouldRemoveFriendAndReturnUser() throws Exception {
        User user1 = User.builder()
                .id(1)
                .email("test1@test.ru")
                .login("test1")
                .name("test1")
                .birthday(LocalDate.of(2001, 1, 1))
                .friends(Set.of())
                .build();

        Mockito.when(userService.removeFriends(1, 2)).thenReturn(user1);

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test1@test.ru"))
                .andExpect(jsonPath("$.login").value("test1"))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.birthday").value("2001-01-01"))
                .andExpect(jsonPath("$.friends").isArray())
                .andExpect(jsonPath("$.friends.length()").value(0));
    }

    @Test
    void shouldReturnFriendsList() throws Exception {
        User friend = User.builder()
                .id(2)
                .email("friend@test.ru")
                .login("friend")
                .name("Friend Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .friends(Set.of())
                .build();

        List<User> friendsList = List.of(friend);

        Mockito.when(userService.getFriendsList(1)).thenReturn(friendsList);

        mockMvc.perform(get("/users/1/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(friend.getId()))
                .andExpect(jsonPath("$[0].email").value(friend.getEmail()))
                .andExpect(jsonPath("$[0].login").value(friend.getLogin()))
                .andExpect(jsonPath("$[0].name").value(friend.getName()))
                .andExpect(jsonPath("$[0].birthday").value(friend.getBirthday().toString()))
                .andExpect(jsonPath("$[0].friends").isArray());
    }

    @Test
    void shouldReturnCommonFriendsList() throws Exception {
        User commonFriend = User.builder()
                .id(3)
                .email("common@test.ru")
                .login("commonFriend")
                .name("Common Friend")
                .birthday(LocalDate.of(1999, 9, 9))
                .friends(Set.of())
                .build();

        List<User> commonFriends = List.of(commonFriend);

        Mockito.when(userService.getCommonFriendsList(1, 2)).thenReturn(commonFriends);

        mockMvc.perform(get("/users/1/friends/common/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(commonFriend.getId()))
                .andExpect(jsonPath("$[0].email").value(commonFriend.getEmail()))
                .andExpect(jsonPath("$[0].login").value(commonFriend.getLogin()))
                .andExpect(jsonPath("$[0].name").value(commonFriend.getName()))
                .andExpect(jsonPath("$[0].birthday").value(commonFriend.getBirthday().toString()))
                .andExpect(jsonPath("$[0].friends").isArray());
    }

}