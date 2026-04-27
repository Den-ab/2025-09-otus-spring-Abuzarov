package ru.otus.hw.services;

import ru.otus.hw.dto.BookDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDTO> findById(long id);

    List<BookDTO> findAll();

    BookDTO insert(String title, long authorId, long genreId);

    BookDTO update(long id, String title, long authorId, long genreId);

    void deleteById(long id);
}
