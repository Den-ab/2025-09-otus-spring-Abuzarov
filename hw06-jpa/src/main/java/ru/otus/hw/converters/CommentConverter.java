package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "Id: %d, content: %s, bookId: %s".formatted(
                comment.getId(),
                comment.getContent(),
                comment.getBook().getId()
        );
    }
}
