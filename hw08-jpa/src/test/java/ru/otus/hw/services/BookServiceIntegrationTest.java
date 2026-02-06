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
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectId testBookId = new ObjectId();

    private List<Genre> genres;

    private List<Author> authors;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("comments");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");

        authors = List.of(
            new Author(new ObjectId(), "Test Author 1"),
            new Author(new ObjectId(), "Test Author 2")
        );
        mongoTemplate.insertAll(authors);
        genres = List.of(
            new Genre(new ObjectId(), "Test Genre 1"),
            new Genre(new ObjectId(), "Test Genre 2")
        );
        mongoTemplate.insertAll(genres);
        mongoTemplate.insert(
            new Book(testBookId, "Test Book 1", authors.get(0).getId(), genres.get(0).getId(), List.of())
        );
    }

    @Test
    @DisplayName("Проверка того что список книг не пустой.")
    void shouldFindAnyBook() {
        final List<BookDTO> allBooks = this.bookService.findAll();
        assertThat(allBooks).isNotEmpty();
    }

    @Test
    @DisplayName("Проверка создания книги.")
    void shouldCreateBook() {

        final ObjectId authorId = authors.get(0).getId();
        final ObjectId genreId = genres.get(0).getId();
        final BookDTO created = this.bookService.insert("Test_book_2", authorId, genreId);
        final Optional<BookDTO> createdBook = this.bookService.findById(new ObjectId(created.id()));
        assertThat(createdBook).isNotEmpty();
        assertThat(createdBook.get().title()).isEqualTo("Test_book_2");
        assertThat(createdBook.get().author().id()).isEqualTo(authorId.toString());
        assertThat(createdBook.get().genre().id()).isEqualTo(genreId.toString());
    }

    @Test
    @DisplayName("Проверка обновления книги.")
    void shouldUpdateBook() {

        final Optional<BookDTO> createdBook = this.bookService.findById(testBookId);
        assertThat(createdBook).isNotEmpty();

        final ObjectId authorId = authors.get(1).getId();
        final ObjectId genreId = genres.get(1).getId();
        this.bookService.update(testBookId, "Test_book_1_edited", authorId, genreId);
        final Optional<BookDTO> updatedBook = this.bookService.findById(testBookId);
        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().title()).isEqualTo("Test_book_1_edited");
        assertThat(updatedBook.get().author().id()).isEqualTo(authorId.toString());
        assertThat(updatedBook.get().genre().id()).isEqualTo(genreId.toString());
    }

    @Test
    @DisplayName("Проверка удаления книги.")
    void shouldDeleteBook() {

        final Optional<BookDTO> createdBook = this.bookService.findById(testBookId);
        assertThat(createdBook).isNotEmpty();
        this.bookService.deleteById(testBookId);
        final Optional<BookDTO> deletedBook = this.bookService.findById(testBookId);
        assertThat(deletedBook).isEmpty();
    }

    @AfterEach
    void clear() {
        mongoTemplate.dropCollection("comments");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
    }
}
