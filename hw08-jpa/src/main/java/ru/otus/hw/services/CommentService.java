package ru.otus.hw.services;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDTO> findById(ObjectId id);

    List<CommentDTO> findByBookId(ObjectId id);

    CommentDTO insert(String content, ObjectId bookId);

    CommentDTO update(ObjectId id, String content, ObjectId bookId);

    void deleteById(ObjectId id);
}
