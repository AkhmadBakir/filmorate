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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
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


@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmService filmService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private Film film;
    private User user;

    @BeforeEach
    void init() {
        film = Film.builder()
                .id(1)
                .name("test1")
                .description("test1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(120)
                .likeUserList(new HashSet<>())
                .disLikeUserList(new HashSet<>())
                .build();

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
    void shouldAddFilmAndReturnFilm() throws Exception {
        Mockito.when(filmService.addFilm(Mockito.any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void updateFilmAndReturnFilm() throws Exception {
        Mockito.when(filmService.updateFilm(Mockito.eq(1), Mockito.any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldReturnAllFilms() throws Exception {
        Film film1 = Film.builder()
                .id(1)
                .name("test1")
                .description("test1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(120)
                .likeUserList(new HashSet<>())
                .disLikeUserList(new HashSet<>())
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("test2")
                .description("test2")
                .releaseDate(LocalDate.of(2002, 2, 2))
                .duration(220)
                .likeUserList(new HashSet<>())
                .disLikeUserList(new HashSet<>())
                .build();

        List<Film> allFilmList = List.of(film1, film2);

        Mockito.when(filmService.allFilms()).thenReturn(allFilmList);

        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allFilmList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allFilmList.size()))
                .andExpect(jsonPath("$[0].id").value(film1.getId()))
                .andExpect(jsonPath("$[0].name").value(film1.getName()))
                .andExpect(jsonPath("$[0].description").value(film1.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film1.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film1.getDuration()))
                .andExpect(jsonPath("$[0].likeUserList").isArray())
                .andExpect(jsonPath("$[0].disLikeUserList").isArray())
                .andExpect(jsonPath("$[1].id").value(film2.getId()))
                .andExpect(jsonPath("$[1].name").value(film2.getName()))
                .andExpect(jsonPath("$[1].description").value(film2.getDescription()))
                .andExpect(jsonPath("$[1].releaseDate").value(film2.getReleaseDate().toString()))
                .andExpect(jsonPath("$[1].duration").value(film2.getDuration()))
                .andExpect(jsonPath("$[1].likeUserList").isArray())
                .andExpect(jsonPath("$[1].disLikeUserList").isArray());
    }

    @Test
    void shouldReturnFilmById() throws Exception {
        Mockito.when(filmService.getFilmById(1)).thenReturn(film);

        mockMvc.perform(get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldAddLikeAndReturnFilm() throws Exception {
        Mockito.when(filmService.getFilmById(1)).thenReturn(film);
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(put("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldDeleteLikeAndReturnFilm() throws Exception {
        Mockito.when(filmService.getFilmById(1)).thenReturn(film);
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(delete("/films/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldAddDisLikeAndReturnFilm() throws Exception {
        Mockito.when(filmService.getFilmById(1)).thenReturn(film);
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(put("/films/1/dislike/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldDeleteDisLikeAndReturnFilm() throws Exception {
        Mockito.when(filmService.getFilmById(1)).thenReturn(film);
        Mockito.when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(delete("/films/1/dislike/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.likeUserList").isArray())
                .andExpect(jsonPath("$.disLikeUserList").isArray());
    }

    @Test
    void shouldReturnTopFilms() throws Exception {
        Film film1 = Film.builder()
                .id(1)
                .name("test1")
                .description("test1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(120)
                .likeUserList(new HashSet<>())
                .disLikeUserList(new HashSet<>())
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("test2")
                .description("test2")
                .releaseDate(LocalDate.of(2002, 2, 2))
                .duration(220)
                .likeUserList(new HashSet<>())
                .disLikeUserList(new HashSet<>())
                .build();

        Set<Film> topFilmList = Set.of(film1, film2);

        Mockito.when(filmService.getTopFilms(2)).thenReturn(topFilmList);

        mockMvc.perform(get("/films/popular?count=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topFilmList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(topFilmList.size()))
                .andExpect(jsonPath("$[0].id").value(film1.getId()))
                .andExpect(jsonPath("$[0].name").value(film1.getName()))
                .andExpect(jsonPath("$[0].description").value(film1.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film1.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film1.getDuration()))
                .andExpect(jsonPath("$[0].likeUserList").isArray())
                .andExpect(jsonPath("$[0].disLikeUserList").isArray())
                .andExpect(jsonPath("$[1].id").value(film2.getId()))
                .andExpect(jsonPath("$[1].name").value(film2.getName()))
                .andExpect(jsonPath("$[1].description").value(film2.getDescription()))
                .andExpect(jsonPath("$[1].releaseDate").value(film2.getReleaseDate().toString()))
                .andExpect(jsonPath("$[1].duration").value(film2.getDuration()))
                .andExpect(jsonPath("$[1].likeUserList").isArray())
                .andExpect(jsonPath("$[1].disLikeUserList").isArray());
    }
}