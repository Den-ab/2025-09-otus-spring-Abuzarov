package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by book id", key = "cbbid")
    public String findCommentByBookId(String bookId) {
        return commentService.findByBookId(new ObjectId(bookId)).stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String content, String bookId) {
        var savedBook = commentService.insert(content, new ObjectId(bookId));
        return commentConverter.commentToString(savedBook);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(String id, String content, String bookId) {
        var savedBook = commentService.update(new ObjectId(id), content, new ObjectId(bookId));
        return commentConverter.commentToString(savedBook);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(new ObjectId(id));
    }
}
