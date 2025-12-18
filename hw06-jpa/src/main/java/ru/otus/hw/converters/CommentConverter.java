package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(CommentDTO comment) {
        return "Id: %d, content: %s, book: %s".formatted(
            comment.id(),
            comment.content(),
            this.bookConverter.bookToString(comment.book())
        );
    }

    public CommentDTO convertToDTO(Comment comment) {

        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            this.bookConverter.convertToDTO(comment.getBook())
        );
    }
}
