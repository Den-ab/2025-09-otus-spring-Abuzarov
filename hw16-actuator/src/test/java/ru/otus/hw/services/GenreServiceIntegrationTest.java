package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.GenreDTO;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class GenreServiceIntegrationTest {

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("Проверка того что список жанров не пустой.")
    void shouldFindAnyGenre() {
        final List<GenreDTO> allGenres = this.genreService.findAll();
        assertThat(allGenres).isNotEmpty();
    }
}
