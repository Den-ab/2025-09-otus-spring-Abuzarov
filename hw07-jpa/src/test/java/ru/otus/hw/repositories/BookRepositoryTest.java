package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Проверка получения книги по ID")
    void shouldFindBookById() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        this.entityManager.flush();

        Optional<Book> actual = this.bookRepository.findById(book.getId());
        Book fromDb = this.entityManager.find(Book.class, book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(fromDb.getTitle());
    }

    @Test
    @DisplayName("Проверка сохранения и обновления книги")
    void shouldInsertAndUpdateBook() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        this.bookRepository.save(book);
        assertThat(book.getId()).isNotNull();

        Book fromDb = this.entityManager.find(Book.class, book.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getTitle()).isEqualTo("Book_1");

        book.setTitle("Book_1_edited");
        this.bookRepository.save(book);
        fromDb = this.entityManager.find(Book.class, book.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getTitle()).isEqualTo("Book_1_edited");
    }

    @Test
    @DisplayName("Проверка удаления книги")
    void shouldDeleteBook() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        entityManager.flush();
        assertThat(book.getId()).isNotNull();
        this.bookRepository.deleteById(book.getId());
        entityManager.flush();
        entityManager.clear();
        Book fromDb = this.entityManager.find(Book.class, book.getId());
        assertThat(fromDb).isNull();
    }
}
