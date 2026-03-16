package ru.otus.hw.processors;

import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Genre;
import ru.otus.hw.entity.GenreSource;

@Component
public class GenreProcessor implements ItemProcessor<GenreSource, MigrationItem<Genre>> {

    @Override
    public MigrationItem<Genre> process(GenreSource item) {

        Genre doc = new Genre();
        doc.setId(new ObjectId());
        doc.setName(item.getName());

        return new MigrationItem<>(item.getId(), doc);
    }
}
