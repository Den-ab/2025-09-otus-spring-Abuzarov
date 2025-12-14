package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookJpaRepository implements BookRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        final Book book = this.entityManager.find(Book.class, id);

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        final TypedQuery<Book> query = this.entityManager.createQuery("select b from Book b", Book.class);

        EntityGraph<?> entityGraph = this.entityManager.getEntityGraph("book-with-authors-and-genres-graph");
        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            this.entityManager.persist(book);
            return book;
        } else {
            return this.entityManager.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {

        final Query query = this.entityManager.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
