package ru.otus.hw.writers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Genre;
import ru.otus.hw.processors.MigrationItem;
import ru.otus.hw.service.IdMappingCache;

@Component
@RequiredArgsConstructor
public class GenreWriter implements ItemWriter<MigrationItem<Genre>> {

    private final MongoTemplate mongoTemplate;

    private final IdMappingCache idMappingCache;

    @Override
    public void write(Chunk<? extends MigrationItem<Genre>> chunk) {

        for (MigrationItem<Genre> migrationItem : chunk.getItems()) {

            Genre savedGenre = mongoTemplate.save(migrationItem.document(), "genres");
            this.idMappingCache.putGenre(migrationItem.sourceId(), savedGenre.getId());
        }
    }
}
