package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.BookRequestDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping(value = "")
    public String findAllAuthorsForHomePage(Model model) {

        final List<BookDTO> books = bookService.findAll();
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping(value = "/books")
    public String findAllBooks(Model model) {

        final List<BookDTO> books = bookService.findAll();
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping(value = "/books/{id}")
    public String findBookById(@PathVariable("id") long id, Model model) {

        final List<AuthorDTO> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        final List<GenreDTO> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        final BookDTO book = bookService.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("No book with id %s", id)));
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping(value = "/books")
    public String insertBook(@ModelAttribute BookRequestDTO book) {
        var savedBook = bookService.insert(book.title(), book.authorId(), book.genreId());

        return "redirect:/books/" + savedBook.id();
    }

    @PostMapping(value = "/books/{id}")
    public String updateBook(@PathVariable("id") long id, @ModelAttribute BookRequestDTO book, Model model) {
        var savedBook = bookService.update(id, book.title(), book.authorId(), book.genreId());

        model.addAttribute("book", savedBook);
        return "book-update-success";
    }

    @DeleteMapping(value = "/books/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);

        return "redirect:/books";
    }
}
