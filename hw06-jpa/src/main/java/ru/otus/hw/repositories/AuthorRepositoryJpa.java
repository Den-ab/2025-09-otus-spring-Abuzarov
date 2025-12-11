package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {

        final TypedQuery<Author> query = this.entityManager.createQuery("select a from Author a", Author.class);

        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {

        final TypedQuery<Author> query = this.entityManager.createQuery(
            "select a from Author a where id = :id",
            Author.class
        );

        query.setParameter("id", id);

        return Optional.ofNullable(query.getSingleResult());
    }
}
