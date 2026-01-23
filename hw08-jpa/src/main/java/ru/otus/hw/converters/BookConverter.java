package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(BookDTO book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
            book.id(),
            book.title(),
            authorConverter.authorToString(book.author()),
            genreConverter.genreToString(book.genre())
        );
    }

    public BookDTO convertToDTO(Book book, Author author, Genre genre, List<Comment> comments) {

        return new BookDTO(
            book.getId().toString(),
            book.getTitle(),
            this.authorConverter.convertToDTO(author),
            this.genreConverter.convertToDTO(genre),
            comments.stream().map(commentConverter::convertToDTO).collect(Collectors.toList())
        );
    }
}
