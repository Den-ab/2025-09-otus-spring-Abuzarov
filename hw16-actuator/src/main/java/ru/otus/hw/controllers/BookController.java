package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.requests.BookCreateRequestDTO;
import ru.otus.hw.dto.requests.BookUpdateRequestDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    private final CommentService commentService;

    @GetMapping(value = "/books")
    public ResponseEntity<List<BookDTO>> findAllBooks() {

        final List<BookDTO> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/books/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable("id") Long id) {

        return bookService.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/books")
    public ResponseEntity<BookDTO> insertBook(@RequestBody BookCreateRequestDTO book) {
        var savedBook = bookService.insert(book.title(), book.authorId(), book.genreId());

        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(value = "/books/{id}")
    public ResponseEntity<BookDTO> updateBook(
        @PathVariable("id") long id,
        @RequestBody BookUpdateRequestDTO book
    ) {
        var savedBook = bookService.update(id, book.title(), book.authorId(), book.genreId());

        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping(value = "/books/{id}")
    public void deleteBook(@PathVariable("id") long id) {

        bookService.deleteById(id);
    }
}
