package ru.otus.hw.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    List<Comment> findCommentsByBookId(ObjectId bookId);
}
