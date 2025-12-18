package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class BookServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("Проверка CRUD операций для книг.")
    void shouldCreateReadUpdateDeleteBook() {

        final long bookId = 1;
        final CommentDTO commentToCreate = this.commentService.insert("Test_comment_1", bookId);
        final Optional<CommentDTO> createdComment = this.commentService.findById(commentToCreate.id());
        assertThat(createdComment).isNotEmpty();
        assertThat(createdComment.get().content()).isEqualTo("Test_comment_1");
        assertThat(createdComment.get().book().id()).isEqualTo(bookId);

        this.commentService.insert("Test_comment_2", bookId);
        final List<CommentDTO> commentsByBookId = this.commentService.findByBookId(bookId);
        assertThat(commentsByBookId).hasSize(2);

        final CommentDTO commentToUpdate = this.commentService.update(
            createdComment.get().id(),
            "Test_comment_1_edited",
            2
        );
        final Optional<CommentDTO> updatedComment = this.commentService.findById(commentToUpdate.id());
        assertThat(updatedComment).isNotEmpty();
        assertThat(updatedComment.get().content()).isEqualTo("Test_comment_1_edited");
        assertThat(updatedComment.get().book().id()).isEqualTo(2);
        this.commentService.deleteById(updatedComment.get().id());
        final Optional<CommentDTO> deletedComment = this.commentService.findById(updatedComment.get().id());
        assertThat(deletedComment).isEmpty();
    }

    @Test
    @DisplayName("Проверка на отсутствие ошибок ленивой загрузки.")
    void doesntThrowLazyInitializationException() {
        final long bookId = 1;
        final CommentDTO commentToCreate = this.commentService.insert("Test_comment_1", bookId);
        assertThatCode(() -> this.commentService.findById(commentToCreate.id())).doesNotThrowAnyException();
    }
}
