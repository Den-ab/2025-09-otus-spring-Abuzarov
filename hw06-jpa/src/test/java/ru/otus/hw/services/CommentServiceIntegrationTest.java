package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.BookDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class CommentServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Проверка того что список книг не пустой.")
    void shouldFindAnyBook() {
        final List<BookDTO> allBooks = this.bookService.findAll();
        assertThat(allBooks).isNotEmpty();
    }

    @Test
    @DisplayName("Проверка CRUD операций для книг.")
    void shouldCreateReadUpdateDeleteBook() {

        final BookDTO bookToCreate = this.bookService.insert("Test_book_1", 2, 2);
        final Optional<BookDTO> createdBook = this.bookService.findById(bookToCreate.id());
        assertThat(createdBook).isNotEmpty();
        assertThat(createdBook.get().title()).isEqualTo("Test_book_1");
        assertThat(createdBook.get().author().id()).isEqualTo(2);
        assertThat(createdBook.get().genre().id()).isEqualTo(2);
        final BookDTO bookToUpdate = this.bookService.update(createdBook.get().id(), "Test_book_1_edited", 3, 3);
        final Optional<BookDTO> updatedBook = this.bookService.findById(bookToUpdate.id());
        assertThat(updatedBook).isNotEmpty();
        assertThat(updatedBook.get().title()).isEqualTo("Test_book_1_edited");
        assertThat(updatedBook.get().author().id()).isEqualTo(3);
        assertThat(updatedBook.get().genre().id()).isEqualTo(3);
        this.bookService.deleteById(updatedBook.get().id());
        final Optional<BookDTO> deletedBook = this.bookService.findById(updatedBook.get().id());
        assertThat(deletedBook).isEmpty();
    }

    @Test
    @DisplayName("Проверка на отсутствие ошибок ленивой загрузки.")
    void doesntThrowLazyInitializationException() {

        assertThatCode(() -> this.bookService.findById(1)).doesNotThrowAnyException();
    }
}
