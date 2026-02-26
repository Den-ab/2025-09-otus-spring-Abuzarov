package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("Проверка получения жанров")
    void shouldReturnGenresViewWIthData() throws Exception {

        GenreDTO genre = new GenreDTO(1L, "Genre_1");
        List<GenreDTO> genres = List.of(genre);

        when(this.genreService.findAll()).thenReturn(genres);

        this.mockMvc.perform(get("/genres").with(user("test.login")))
            .andExpect(status().isOk())
            .andExpect(view().name("genres"))
            .andExpect(model().attributeExists("genres"))
            .andExpect(model().attribute("genres", genres));
    }
}
