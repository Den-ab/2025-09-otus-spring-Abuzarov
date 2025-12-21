package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(AuthorJpaRepository.class)
public class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

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
}
