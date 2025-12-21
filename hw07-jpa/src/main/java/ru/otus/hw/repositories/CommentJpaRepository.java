package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentJpaRepository implements CommentRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        final Comment comment = this.entityManager.find(Comment.class, id);

        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {

        final TypedQuery<Comment> query = this.entityManager.createQuery(
            "select c from Comment c where c.book.id = :id",
            Comment.class
        );
        query.setParameter("id", bookId);

        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            this.entityManager.persist(comment);
            return comment;
        } else {
            return this.entityManager.merge(comment);
        }
    }

    @Override
    public void deleteById(long id) {

        final Query query = this.entityManager.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
