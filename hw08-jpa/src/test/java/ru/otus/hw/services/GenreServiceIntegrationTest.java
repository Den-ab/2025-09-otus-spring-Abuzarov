package ru.otus.hw.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class GenreServiceIntegrationTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("genres");

        mongoTemplate.insertAll(
            List.of(new Genre(new ObjectId(), "Test Genre 1"), new Genre(new ObjectId(), "Test Genre 2"))
        );
    }

    @Test
    @DisplayName("Проверка того что список жанров не пустой.")
    void shouldFindAnyGenre() {
        final List<GenreDTO> allGenres = this.genreService.findAll();
        assertThat(allGenres).isNotEmpty();
    }

    @AfterEach
    void clear() {
        mongoTemplate.dropCollection("genres");
    }
}
