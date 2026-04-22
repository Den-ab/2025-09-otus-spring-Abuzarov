package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDTO book) {
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
            book.id(),
            book.title(),
            authorConverter.authorToString(book.author()),
            genreConverter.genreToString(book.genre())
        );
    }

    public BookDTO convertToDTO(Book book) {

        return new BookDTO(
            book.getId(),
            book.getTitle(),
            this.authorConverter.convertToDTO(book.getAuthor()),
            this.genreConverter.convertToDTO(book.getGenre())
        );
    }
}
