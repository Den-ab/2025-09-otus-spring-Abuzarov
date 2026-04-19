package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Book;
import ru.otus.hw.entity.BookSource;
import ru.otus.hw.service.IdMappingCache;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<BookSource, MigrationItem<Book>> {

    private final IdMappingCache idMappingCache;

    @Override
    public MigrationItem<Book> process(BookSource item) {

        Long authorSourceId = item.getAuthorSource().getId();
        Long genreSourceId = item.getGenreSource().getId();

        ObjectId authorTargetId = Optional.ofNullable(idMappingCache.getAuthor(authorSourceId))
            .orElseThrow(() -> new IllegalStateException("Author mapping not found for sourceId=" + authorSourceId));
        ObjectId genreTargetId = Optional.ofNullable(idMappingCache.getGenre(genreSourceId))
            .orElseThrow(() -> new IllegalStateException("Genre mapping not found for sourceId=" + genreSourceId));

        Book doc = new Book();
        doc.setId(new ObjectId());
        doc.setTitle(item.getTitle());
        doc.setAuthorId(authorTargetId);
        doc.setGenreId(genreTargetId);

        return new MigrationItem<>(item.getId(), doc);
    }
}
