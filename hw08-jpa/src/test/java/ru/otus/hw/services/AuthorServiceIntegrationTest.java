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
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("authors");

        mongoTemplate.insertAll(
            List.of(new Author(new ObjectId(), "Test Author 1"), new Author(new ObjectId(), "Test Author 2"))
        );
    }

    @Test
    @DisplayName("Проверка того что список авторов не пустой.")
    void shouldFindAnyAuthor() {
        final List<AuthorDTO> allAuthors = this.authorService.findAll();
        assertThat(allAuthors).isNotEmpty();
    }

    @AfterEach
    void clear() {
        mongoTemplate.dropCollection("authors");
    }
}
