package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreJpaRepository implements GenreRepository {

    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        final TypedQuery<Genre> query = this.entityManager.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        final Genre author = this.entityManager.find(Genre.class, id);
        return Optional.ofNullable(author);
    }
}
