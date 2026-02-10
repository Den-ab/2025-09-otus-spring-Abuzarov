package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.dto.requests.BookCreateRequestDTO;
import ru.otus.hw.dto.requests.BookUpdateRequestDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private ObjectMapper objectMapper;

    private final BookDTO exampleBook = new BookDTO(
        1L,
        "Book_1",
        new AuthorDTO(1L, "Author_1"),
        new GenreDTO(1L, "Genre_1")
    );

    @Test
    @DisplayName("Проверка получения книг для домашней страницы")
    void shouldReturnBooksForHomePage() throws Exception {

        BookDTO book = new BookDTO(1L, "Book_1", new AuthorDTO(1L, "A1"), new GenreDTO(1L, "G1"));
        List<BookDTO> books = List.of(book);
        when(this.bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/book"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("Проверка получения книг")
    void shouldReturnBooks() throws Exception {

        BookDTO book = new BookDTO(1L, "Book_1", new AuthorDTO(1L, "A1"), new GenreDTO(1L, "G1"));
        List<BookDTO> books = List.of(book);
        when(this.bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("Проверка получения книги по ее ID")
    void shouldReturnBookById() throws Exception {

        BookDTO book = new BookDTO(1L, "Book_1", new AuthorDTO(1L, "A1"), new GenreDTO(1L, "G1"));

        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    @DisplayName("Проверка создания книги")
    void shouldCreateBook() throws Exception {

        BookCreateRequestDTO request = new BookCreateRequestDTO("New Book", 1L, 1L);
        BookDTO savedBook = new BookDTO(1L, "New Book", new AuthorDTO(1L, "A1"), new GenreDTO(1L, "G1"));

        when(bookService.insert("New Book", 1L, 1L)).thenReturn(savedBook);

        mockMvc.perform(
            post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(savedBook)));
    }

    @Test
    @DisplayName("Проверка обновления книги")
    void shouldUpdateBook() throws Exception {

        BookUpdateRequestDTO request = new BookUpdateRequestDTO(1L, "Updated Book", 1L, 1L);
        BookDTO updatedBook = new BookDTO(1L, "Updated Book", new AuthorDTO(1L, "A1"), new GenreDTO(1L, "G1"));

        when(bookService.update(1L, "Updated Book", 1L, 1L)).thenReturn(updatedBook);

        mockMvc.perform(
            put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(updatedBook)));
    }

    @Test
    @DisplayName("Проверка удаления книги и редиректить на список")
    void shouldDeleteBook() throws Exception {

        mockMvc.perform(delete("/book/1")).andExpect(status().isOk());

        verify(bookService).deleteById(1L);
    }
}
