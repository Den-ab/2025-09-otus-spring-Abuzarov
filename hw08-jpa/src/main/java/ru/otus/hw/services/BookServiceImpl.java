package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
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

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @Transactional
    @Override
    public Optional<BookDTO> findById(ObjectId id) {
        return bookRepository.findById(id)
            .map(book -> {
                final Author author = authorRepository.findById(book.getAuthorId()).orElseThrow(() ->
                    new EntityNotFoundException("Author with id %s not found".formatted(book.getAuthorId()))
                );
                final Genre genre = genreRepository.findById(book.getGenreId()).orElseThrow(() ->
                    new EntityNotFoundException("Genre with id %s not found".formatted(book.getGenreId()))
                );
                final List<Comment> comments = commentRepository.findAllById(book.getComments());
                return this.bookConverter.convertToDTO(book, author, genre, comments);
            });
    }

    @Transactional
    @Override
    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
            .map(book -> {
                final Author author = authorRepository.findById(book.getAuthorId()).orElseThrow(() ->
                    new EntityNotFoundException("Author with id %s not found".formatted(book.getAuthorId()))
                );
                final Genre genre = genreRepository.findById(book.getGenreId()).orElseThrow(() ->
                    new EntityNotFoundException("Genre with id %s not found".formatted(book.getGenreId()))
                );
                final List<Comment> comments = commentRepository.findAllById(book.getComments());
                return this.bookConverter.convertToDTO(book, author, genre, comments);
            })
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDTO insert(String title, ObjectId authorId, ObjectId genreId) {
        final Author author = authorRepository.findById(authorId).orElseThrow(() ->
            new EntityNotFoundException("Author with id %s not found".formatted(authorId))
        );
        final Genre genre = genreRepository.findById(genreId).orElseThrow(() ->
            new EntityNotFoundException("Genre with id %s not found".formatted(genreId))
        );
        final Book savedBook = save(null, title, authorId, genreId);
        return this.bookConverter.convertToDTO(savedBook, author, genre, List.of());
    }

    @Transactional
    @Override
    public BookDTO update(ObjectId id, String title, ObjectId authorId, ObjectId genreId) {

        final Author author = authorRepository.findById(authorId).orElseThrow(() ->
            new EntityNotFoundException("Author with id %s not found".formatted(authorId))
        );
        final Genre genre = genreRepository.findById(genreId).orElseThrow(() ->
            new EntityNotFoundException("Genre with id %s not found".formatted(genreId))
        );
        final Book updatedBook = save(id, title, authorId, genreId);
        final List<Comment> comments = commentRepository.findAllById(updatedBook.getComments());

        return this.bookConverter.convertToDTO(updatedBook, author, genre, comments);
    }

    @Transactional
    @Override
    public void deleteById(ObjectId id) {
        bookRepository.deleteById(id);
    }

    private Book save(ObjectId bookId, String title, ObjectId authorId, ObjectId genreId) {
        Optional.ofNullable(authorId).flatMap(authorRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        Optional.ofNullable(genreId).flatMap(genreRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        final Book existBook = Optional.ofNullable(bookId).flatMap(bookRepository::findById).orElse(null);
        var book = new Book(
            bookId,
            title,
            authorId,
            genreId,
            Optional.ofNullable(existBook).map(Book::getComments).orElse(List.of())
        );
        return bookRepository.save(book);
    }
}
