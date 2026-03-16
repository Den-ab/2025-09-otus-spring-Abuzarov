package ru.otus.hw.writers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Author;
import ru.otus.hw.processors.MigrationItem;
import ru.otus.hw.service.IdMappingCache;

@Component
@RequiredArgsConstructor
public class AuthorWriter implements ItemWriter<MigrationItem<Author>> {

    private final MongoTemplate mongoTemplate;

    private final IdMappingCache idMappingCache;

    @Override
    public void write(Chunk<? extends MigrationItem<Author>> chunk) {

        for (MigrationItem<Author> migrationItem : chunk.getItems()) {

            Author savedAuthor = mongoTemplate.save(migrationItem.document(), "authors");
            this.idMappingCache.putAuthor(migrationItem.sourceId(), savedAuthor.getId());
        }
    }
}
