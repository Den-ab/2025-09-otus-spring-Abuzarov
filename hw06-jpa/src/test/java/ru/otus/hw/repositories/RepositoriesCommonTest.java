package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({BookJpaRepository.class, AuthorJpaRepository.class, GenreJpaRepository.class, CommentJpaRepository.class})
public class RepositoriesCommonTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

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

    @Test
    @DisplayName("Проверка получения автора по ID")
    void shouldFindAuthorById() {
        final Author author = this.entityManager.persist(new Author(null, "Author_1"));
        this.entityManager.flush();

        Optional<Author> actual = this.authorRepository.findById(author.getId());
        Author fromDb = this.entityManager.find(Author.class, author.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getFullName()).isEqualTo(fromDb.getFullName());
    }

    @Test
    @DisplayName("Проверка получения жанра по ID")
    void shouldFindGenreById() {
        final Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        this.entityManager.flush();

        Optional<Genre> actual = this.genreRepository.findById(genre.getId());
        Genre fromDb = this.entityManager.find(Genre.class, genre.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(fromDb.getName());
    }

    @Test
    @DisplayName("Проверка получения коммента по ID")
    void shouldFindCommentByIdAndBookId() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        Comment comment = this.entityManager.persist(new Comment(null, "Comment_1", book));
        this.entityManager.flush();

        Optional<Comment> actual = this.commentRepository.findById(comment.getId());
        Comment fromDb = this.entityManager.find(Comment.class, comment.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getContent()).isEqualTo(fromDb.getContent());

        final List<Comment> byBookId = this.commentRepository.findByBookId(book.getId());
        assertThat(byBookId).isNotEmpty();
    }

    @Test
    @DisplayName("Проверка сохранения и обновления коммента")
    void shouldInsertAndUpdateComment() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        Comment comment = this.entityManager.persist(new Comment(null, "Comment_1", book));
        this.commentRepository.save(comment);
        assertThat(comment.getId()).isNotNull();

        Comment fromDb = this.entityManager.find(Comment.class, comment.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getContent()).isEqualTo("Comment_1");

        comment.setContent("Comment_1_edited");
        this.commentRepository.save(comment);
        fromDb = this.entityManager.find(Comment.class, comment.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getContent()).isEqualTo("Comment_1_edited");
    }

    @Test
    @DisplayName("Проверка удаления коммента")
    void shouldDeleteComment() {
        Author author = this.entityManager.persist(new Author(null, "Author_1"));
        Genre genre = this.entityManager.persist(new Genre(null, "Genre_1"));
        Book book = this.entityManager.persist(new Book(null, "Book_1", author, genre));
        Comment comment = this.entityManager.persist(new Comment(null, "Comment_1", book));
        entityManager.flush();
        assertThat(comment.getId()).isNotNull();
        this.commentRepository.deleteById(comment.getId());
        entityManager.flush();
        entityManager.clear();
        Comment fromDb = this.entityManager.find(Comment.class, comment.getId());
        assertThat(fromDb).isNull();
    }
}
