package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Transactional
    @Override
    public Optional<BookDTO> findById(long id) {
        return bookRepository.findById(id).map(this.bookConverter::convertToDTO);
    }

    @Transactional
    @Override
    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream().map(this.bookConverter::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDTO insert(String title, long authorId, long genreId) {
        return this.bookConverter.convertToDTO(save(null, title, authorId, genreId));
    }

    @Transactional
    @Override
    public BookDTO update(long id, String title, long authorId, long genreId) {
        return this.bookConverter.convertToDTO(save(id, title, authorId, genreId));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
