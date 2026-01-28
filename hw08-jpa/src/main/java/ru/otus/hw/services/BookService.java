package ru.otus.hw.services;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.BookDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDTO> findById(ObjectId id);

    List<BookDTO> findAll();

    BookDTO insert(String title, ObjectId authorId, ObjectId genreId);

    BookDTO update(ObjectId id, String title, ObjectId authorId, ObjectId genreId);

    void deleteById(ObjectId id);
}
