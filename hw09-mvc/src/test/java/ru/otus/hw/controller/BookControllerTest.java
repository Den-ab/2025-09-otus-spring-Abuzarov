package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private  GenreService genreService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private CommentService commentService;

    private final BookDTO exampleBook = new BookDTO(
        1L,
        "Book_1",
        new AuthorDTO(1L, "Author_1"),
        new GenreDTO(1L, "Genre_1")
    );

    @BeforeEach
    void addBooks() {

        List<BookDTO> books = List.of(exampleBook);
        when(this.bookService.findAll()).thenReturn(books);
        when(this.bookService.findById(1L)).thenReturn(Optional.of(exampleBook));
    }
    @Test
    @DisplayName("Проверка получения книг для домашней страницы")
    void shouldReturnBooksViewDataForHomePage() throws Exception {

        this.mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", this.bookService.findAll()));
    }

    @Test
    @DisplayName("Проверка получения книг")
    void shouldReturnBooksViewData() throws Exception {

        this.mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", this.bookService.findAll()));
    }

    @Test
    @DisplayName("Проверка получения книги по ее ID")
    void shouldReturnBookById() throws Exception {

        this.mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("book"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attribute("book", this.exampleBook));
    }

    @Test
    @DisplayName("Проверка редиректа при создании книги")
    void shouldInsertBookAndRedirect() throws Exception {

        final String title = "Book_2";
        final AuthorDTO author = new AuthorDTO(2L, "Author_2");
        final GenreDTO genre = new GenreDTO(2L, "Genre_2");
        BookDTO savedBook = new BookDTO(2L, title, author, genre);
        when(this.bookService.insert(title, author.id(), genre.id())).thenReturn(savedBook);

        this.mockMvc.perform(
            post("/books")
                .param("id", String.valueOf(savedBook.id()))
                .param("title", title)
                .param("authorId", String.valueOf(author.id()))
                .param("genreId", String.valueOf(genre.id()))
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books/" + savedBook.id()));
    }

    @Test
    @DisplayName("Проверка обновления книги")
    void shouldUpdateBook() throws Exception {

        final String title = "Book_2_edited";
        final AuthorDTO author = new AuthorDTO(2L, "Author_2");
        final GenreDTO genre = new GenreDTO(2L, "Genre_2");
        BookDTO updatedBook = new BookDTO(2L, title, author, genre);
        when(this.bookService.update(updatedBook.id(), title, author.id(), genre.id())).thenReturn(updatedBook);

        this.mockMvc.perform(
                post("/books/{id}", updatedBook.id())
                    .param("id", String.valueOf(updatedBook.id()))
                    .param("title", title)
                    .param("authorId", String.valueOf(author.id()))
                    .param("genreId", String.valueOf(genre.id()))
            )
            .andExpect(status().isOk())
            .andExpect(view().name("book-update-success"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attribute("book", updatedBook));
    }

    @Test
    @DisplayName("Проверка удаления книги и редиректить на список")
    void shouldDeleteBook() throws Exception {

        long bookId = 1L;
        this.mockMvc.perform(delete("/books/{id}", bookId))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books"));

        verify(this.bookService).deleteById(bookId);
    }
}
