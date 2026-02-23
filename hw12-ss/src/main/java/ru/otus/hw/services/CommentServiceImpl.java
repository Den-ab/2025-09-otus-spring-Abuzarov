package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

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
    public Optional<CommentDTO> findById(long id) {
        return commentRepository.findById(id).map(this.commentConverter::convertToDTO);
    }

    @Transactional
    @Override
    public List<CommentDTO> findByBookId(long id) {
        return commentRepository.findByBookId(id).stream()
            .map(this.commentConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDTO insert(String content, long bookId) {
        return this.commentConverter.convertToDTO(save(null, content, bookId));
    }

    @Transactional
    @Override
    public CommentDTO update(long id, String content, long bookId) {
        return this.commentConverter.convertToDTO(save(id, content, bookId));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(Long id, String content, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, content, book);
        return commentRepository.save(comment);
    }
}
