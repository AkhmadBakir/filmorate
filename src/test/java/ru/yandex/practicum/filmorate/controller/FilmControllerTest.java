//package ru.yandex.practicum.filmorate.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.awt.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class FilmControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
////    @LocalServerPort
////    private int port;
////
////    @Autowired
////    private TestRestTemplate testRestTemplate;
//
//    private Film film;
//
//    @BeforeEach
//    void createFilm() {
//        film = Film.builder()
//                .name("Test")
//                .description("Test description")
//                .releaseDate(LocalDate.of(2000, 12, 12))
//                .duration(100)
//                .build();
//    }
//
////    @Test
////    void shouldAddFilm() {
////        String url = "http://localhost:" + port + "/films";
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
////        assertThat(response.getBody()).isNotNull();
////        assertThat(response.getBody().getId()).isNotNull();
////        assertThat(response.getBody().getName()).isEqualTo("Test");
////        assertThat(response.getBody().getDescription()).isEqualTo("Test description");
////    }
////
////    @Test
////    void shouldValidationNameFilm() {
////        String url = "http://localhost:" + port + "/films";
////        film.setName("");
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
////    }
////
////    @Test
////    void shouldValidationDescriptionFilm() {
////        String url = "http://localhost:" + port + "/films";
////        film.setDescription("Description".repeat(200));
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
////    }
////
////    @Test
////    void shouldValidationReleaseDateFilm() {
////        String url = "http://localhost:" + port + "/films";
////        film.setReleaseDate(LocalDate.of(1895, 12, 27));
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
////    }
////
////    @Test
////    void shouldValidationDurationFilm() {
////        String url = "http://localhost:" + port + "/films";
////        film.setDuration(0);
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
////    }
//
//
//    @Test
//    void shouldUpdateFilm() throws Exception {
////        String url = "http://localhost:" + port + "/films";
////        ResponseEntity<Film> response = testRestTemplate.postForEntity(url, film, Film.class);
////        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
////        film.setId(1);
////        film.setName("Test1");
////        film.setDescription("Test1 description1");
////        film.setReleaseDate(LocalDate.of(2020, 1, 1));
////        film.setDuration(200);
////        testRestTemplate.put(url, film, Film.class);
////        ResponseEntity<List> response1 = testRestTemplate.getForEntity(url, List.class);
////        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
////        assertThat(response1.getBody()).isNotNull();
////        assertThat(response1.getBody().getId()).isNotNull();
////        assertThat(response1.getBody().getName()).isEqualTo("Test1");
////        assertThat(response1.getBody().getDescription()).isEqualTo("Test description1");
//
//        mockMvc.perform(post("/films")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(film)))
//                .andExpect(status().isOk());
//
//        film.setName("updateTest");
//        mockMvc.perform(put("/films")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(film)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("updateTest"));
//    }
//
//    @Test
//    void shouldGetAllFilms() {
//    }
//}