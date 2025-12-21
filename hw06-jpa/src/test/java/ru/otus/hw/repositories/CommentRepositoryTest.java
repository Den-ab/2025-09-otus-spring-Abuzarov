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
@Import(CommentJpaRepository.class)
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

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
