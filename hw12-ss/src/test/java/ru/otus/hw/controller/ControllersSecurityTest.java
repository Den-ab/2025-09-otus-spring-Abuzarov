package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({ BookController.class, AuthorController.class, GenreController.class })
@Import(SecurityConfiguration.class)
public class ControllersSecurityTest {

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
        when(this.bookService.insert(anyString(), anyLong(), anyLong())).thenReturn(exampleBook);
        when(this.bookService.update(anyLong(), anyString(), anyLong(), anyLong())).thenReturn(exampleBook);
    }

    @DisplayName("Должен вернуть соответствующий статус")
    @ParameterizedTest(name = "{index} ==> {0} {1}, user: {2}, status: {3}")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(
        String method,
        String uri,
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userPostProcessor,
        ResultMatcher expectedStatus,
        String expectedViewOrRedirect,
        Map<String, String> params
    ) throws Exception {

        var request = method2RequestBuilder(method, uri);
        if (userPostProcessor != null) {
            request.with(userPostProcessor);
        }

        params.forEach(request::param);
        var result = this.mockMvc.perform(request).andExpect(expectedStatus);

        if (expectedViewOrRedirect != null) {
            if (expectedViewOrRedirect.startsWith("redirect:")) {
                String expectedUrl = expectedViewOrRedirect.substring(9);
                result.andExpect(redirectedUrl(expectedUrl));
            } else if (expectedViewOrRedirect.contains("login")) {
                result.andExpect(redirectedUrlPattern("**/login"));
            } else {
                result.andExpect(view().name(expectedViewOrRedirect));
            }
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String uri) {

        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
            "get", MockMvcRequestBuilders::get,
            "post", MockMvcRequestBuilders::post,
            "put", MockMvcRequestBuilders::put,
            "delete", MockMvcRequestBuilders::delete
        );

        return methodMap.get(method).apply(uri);
    }

    public static Stream<Arguments> getTestData() {
        var authUser = user("test.login");
        Map<String, String> bookParams = Map.of(
            "id", "2",
            "title", "Book_2",
            "authorId", "2",
            "genreId", "2"
        );

        return Stream.of(
            Arguments.of("get", "/authors", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/genres", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/books", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("post", "/books", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/", authUser, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/authors", authUser, status().isOk(), "authors", Map.of()),
            Arguments.of("get", "/genres", authUser, status().isOk(), "genres", Map.of()),
            Arguments.of("get", "/books", authUser, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/books/1", authUser, status().isOk(), "update-book", Map.of()),
            Arguments.of("post", "/books", authUser, status().isOk(), "book-save-success", bookParams),
            Arguments.of("post", "/books/2", authUser, status().isOk(), "book-save-success", bookParams),
            Arguments.of("delete", "/books/1", authUser, status().is3xxRedirection(), "redirect:/books", Map.of())
        );
    }
}
