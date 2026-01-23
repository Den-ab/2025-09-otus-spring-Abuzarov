package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @Transactional
    @Override
    public Optional<CommentDTO> findById(ObjectId id) {
        return commentRepository.findById(id).map(this.commentConverter::convertToDTO);
    }

    @Transactional
    @Override
    public List<CommentDTO> findByBookId(ObjectId id) {
        final Optional<Book> book = bookRepository.findById(id);
        final List<ObjectId> commentIds = book.map(Book::getComments).orElseGet(ArrayList::new);
        return commentRepository.findAllById(commentIds).stream()
            .map(this.commentConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDTO insert(String content, ObjectId bookId) {
        return this.commentConverter.convertToDTO(save(null, content, bookId));
    }

    @Transactional
    @Override
    public CommentDTO update(ObjectId id, String content, ObjectId bookId) {
        return this.commentConverter.convertToDTO(save(id, content, bookId));
    }

    @Transactional
    @Override
    public void deleteById(ObjectId id) {
        commentRepository.deleteById(id);
    }

    private Comment save(ObjectId id, String content, ObjectId bookId) {
        bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, content, bookId);
        return commentRepository.save(comment);
    }
}
