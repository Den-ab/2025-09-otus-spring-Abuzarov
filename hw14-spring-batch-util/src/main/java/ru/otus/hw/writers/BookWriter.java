package ru.otus.hw.writers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Book;
import ru.otus.hw.processors.MigrationItem;
import ru.otus.hw.service.IdMappingCache;

@Component
@RequiredArgsConstructor
public class BookWriter implements ItemWriter<MigrationItem<Book>> {

    private final MongoTemplate mongoTemplate;

    private final IdMappingCache idMappingCache;

    @Override
    public void write(Chunk<? extends MigrationItem<Book>> chunk) {

        for (MigrationItem<Book> migrationItem : chunk.getItems()) {

            Book savedBook = mongoTemplate.save(migrationItem.document(), "books");
            this.idMappingCache.putBook(migrationItem.sourceId(), savedBook.getId());
        }
    }
}
