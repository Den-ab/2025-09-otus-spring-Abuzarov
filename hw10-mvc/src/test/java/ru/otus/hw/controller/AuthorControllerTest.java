package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @Test
    @DisplayName("Проверка получения авторов")
    void shouldReturnAuthors() throws Exception {

        AuthorDTO author = new AuthorDTO(1L, "Author_1");
        List<AuthorDTO> authors = List.of(author);

        when(this.authorService.findAll()).thenReturn(authors);

        this.mockMvc.perform(get("/author"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(authors)));
    }
}
