package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
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
    void setUpMocks() {
        when(bookService.findAll()).thenReturn(List.of(exampleBook));
        when(bookService.findById(1L)).thenReturn(Optional.of(exampleBook));
        when(bookService.update(anyLong(), anyString(), anyLong(), anyLong())).thenReturn(exampleBook);

        doAnswer(invocation -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return null;
            }
            boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
            if (!admin) {
                throw new AccessDeniedException("ACL Denied");
            }
            return null;
        }).when(bookService).deleteById(1L);

        doAnswer(invocation -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return exampleBook;
            }
            boolean hasRight = auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_SUPER_ADMIN") || a.getAuthority().equals("ROLE_USER_EDITOR")
            );
            if (!hasRight) {
                throw new AccessDeniedException("Forbidden");
            }
            return exampleBook;
        }).when(bookService).insert(any(), anyLong(), anyLong());

        doAnswer(invocation -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return exampleBook;
            }
            boolean canUpdate = auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_SUPER_ADMIN") || a.getAuthority().equals("ROLE_USER_EDITOR")
            );
            if (!canUpdate) {
                throw new org.springframework.security.access.AccessDeniedException("Forbidden");
            }
            return exampleBook;
        }).when(bookService).update(anyLong(), anyString(), anyLong(), anyLong());

        when(authorService.findAll()).thenAnswer(invocation -> {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                throw new AccessDeniedException("Unauthenticated");
            }
            return List.of(new AuthorDTO(1L, "Author_1"));
        });
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
        var admin = user("test.admin").roles("SUPER_ADMIN");
        var editor = user("test.editor").roles("USER_EDITOR");
        var observer = user("test.observer").roles("USER_OBSERVER");
        Map<String, String> bookParamsToCreate = Map.of(
            "title", "Book_2",
            "authorId", "2",
            "genreId", "2"
        );
        Map<String, String> bookParamsToUpdate = Map.of(
            "id", "2",
            "title", "Book_2",
            "authorId", "2",
            "genreId", "2"
        );

        return Stream.of(
            Arguments.of("get", "/authors", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/authors", admin, status().isOk(), "authors", Map.of()),
            Arguments.of("get", "/authors", editor, status().isOk(), "authors", Map.of()),
            Arguments.of("get", "/authors", observer, status().isOk(), "authors", Map.of()),

            Arguments.of("get", "/genres", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/genres", admin, status().isOk(), "genres", Map.of()),
            Arguments.of("get", "/genres", editor, status().isOk(), "genres", Map.of()),
            Arguments.of("get", "/genres", observer, status().isOk(), "genres", Map.of()),


            Arguments.of("get", "/books", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/books", admin, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/books", editor, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/books", observer, status().isOk(), "index", Map.of()),

            Arguments.of("get", "/", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/", admin, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/", editor, status().isOk(), "index", Map.of()),
            Arguments.of("get", "/", observer, status().isOk(), "index", Map.of()),

            Arguments.of("get", "/books/1", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("get", "/books/1", admin, status().isOk(), "update-book", Map.of()),
            Arguments.of("get", "/books/1", editor, status().isOk(), "update-book", Map.of()),
            Arguments.of("get", "/books/1", observer, status().isOk(), "update-book", Map.of()),

            Arguments.of("post", "/books", null, status().is3xxRedirection(), null, bookParamsToCreate),
            Arguments.of("post", "/books", admin, status().isOk(), "book-save-success", bookParamsToCreate),
            Arguments.of("post", "/books", editor, status().isOk(), "book-save-success", bookParamsToCreate),
            Arguments.of("post", "/books", observer, status().is4xxClientError(), null, bookParamsToCreate),

            Arguments.of("post", "/books/2", null, status().is3xxRedirection(), null, bookParamsToUpdate),
            Arguments.of("post", "/books/2", admin, status().isOk(), "book-save-success", bookParamsToUpdate),
            Arguments.of("post", "/books/2", editor, status().isOk(), "book-save-success", bookParamsToUpdate),
            Arguments.of("post", "/books/2", observer, status().is4xxClientError(), null, bookParamsToUpdate),

            Arguments.of("delete", "/books/1", null, status().is3xxRedirection(), null, Map.of()),
            Arguments.of("delete", "/books/1", admin, status().is3xxRedirection(), "redirect:/books", Map.of()),
            Arguments.of("delete", "/books/1", editor, status().is4xxClientError(), null, Map.of()),
            Arguments.of("delete", "/books/1", observer, status().is4xxClientError(), null, Map.of())
        );
    }
}
