package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    public String findCommentByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    public String insertComment(String content, long bookId) {
        var savedBook = commentService.insert(content, bookId);
        return commentConverter.commentToString(savedBook);
    }

    public String updateComment(long id, String content, long bookId) {
        var savedBook = commentService.update(id, content, bookId);
        return commentConverter.commentToString(savedBook);
    }

    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
