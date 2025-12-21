package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(GenreJpaRepository.class)
public class GenreRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository genreRepository;

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
}
