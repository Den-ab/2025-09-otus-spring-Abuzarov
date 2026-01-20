package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.AuthorDTO;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("Проверка того что список авторов не пустой.")
    void shouldFindAnyAuthor() {
        final List<AuthorDTO> allAuthors = this.authorService.findAll();
        assertThat(allAuthors).isNotEmpty();
    }
}
