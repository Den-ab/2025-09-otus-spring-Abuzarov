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
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectId testBookId = new ObjectId();

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("comments");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("authors");
        final Genre genre = new Genre(new ObjectId(), "Test Genre 1");
        final Author author = new Author(new ObjectId(), "Test Author 1");
        mongoTemplate.insert(genre);
        mongoTemplate.insert(author);
        mongoTemplate.insert(new Book(testBookId, "Test Book 1", author.getId(), genre.getId(), List.of()));
    }

    @Test
    @DisplayName("Проверка создания коммента.")
    void shouldCreateComment() {

        final Optional<BookDTO> book = this.bookService.findById(testBookId);
        assertThat(book).isNotEmpty();
        final CommentDTO commentToCreate = this.commentService.insert("Test_comment_1", testBookId);
        final Optional<CommentDTO> createdComment = this.commentService.findById(new ObjectId(commentToCreate.id()));
        assertThat(createdComment).isNotEmpty();
        assertThat(createdComment.get().content()).isEqualTo("Test_comment_1");
        assertThat(new ObjectId(createdComment.get().bookId())).isEqualTo(testBookId);

        this.commentService.insert("Test_comment_2", testBookId);
        final List<CommentDTO> commentsByBookId = this.commentService.findByBookId(testBookId);
        assertThat(commentsByBookId).hasSize(2);
    }

    @Test
    @DisplayName("Проверка обновления коммента.")
    void shouldUpdateComment() {

        final Optional<BookDTO> book = this.bookService.findById(testBookId);
        assertThat(book).isNotEmpty();
        final CommentDTO commentToCreate = this.commentService.insert("Test_comment_1", testBookId);
        final Optional<CommentDTO> createdComment = this.commentService.findById(new ObjectId(commentToCreate.id()));
        assertThat(createdComment).isNotEmpty();
        final ObjectId newBookId = new ObjectId();
        mongoTemplate.insert(
            new Book(
                newBookId,
                "Test Book 2",
                new ObjectId(book.get().author().id()),
                new ObjectId(book.get().genre().id()),
                List.of()
            )
        );
        final CommentDTO commentToUpdate = this.commentService.update(
            new ObjectId(createdComment.get().id()),
            "Edit_comment_1",
            newBookId
        );
        final Optional<CommentDTO> updatedComment = this.commentService.findById(new ObjectId(commentToUpdate.id()));
        assertThat(updatedComment).isNotEmpty();
        assertThat(updatedComment.get().content()).isEqualTo("Edit_comment_1");
        assertThat(updatedComment.get().bookId()).isEqualTo(newBookId.toString());
    }

    @Test
    @DisplayName("Проверка удаления коммента.")
    void shouldDeleteComment() {

        final Optional<BookDTO> book = this.bookService.findById(testBookId);
        assertThat(book).isNotEmpty();
        final CommentDTO commentToCreate = this.commentService.insert("Test_comment_1", testBookId);
        final Optional<CommentDTO> createdComment = this.commentService.findById(new ObjectId(commentToCreate.id()));
        assertThat(createdComment).isNotEmpty();
        this.commentService.deleteById(new ObjectId(createdComment.get().id()));
        final Optional<CommentDTO> deletedComment = this.commentService.findById(
            new ObjectId(createdComment.get().id())
        );
        assertThat(deletedComment).isEmpty();
    }

    @AfterEach
    void clear() {
        mongoTemplate.dropCollection("comments");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
    }
}
