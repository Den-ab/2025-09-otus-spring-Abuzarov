package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(CommentDTO comment) {
        return "Id: %s, content: %s, book id: %s".formatted(
            comment.id(),
            comment.content(),
            comment.bookId()
        );
    }

    public CommentDTO convertToDTO(Comment comment) {

        return new CommentDTO(comment.getId().toString(), comment.getContent(), comment.getBookId().toString());
    }
}
