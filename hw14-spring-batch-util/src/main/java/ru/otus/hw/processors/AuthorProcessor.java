package ru.otus.hw.processors;

import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.document.Author;
import ru.otus.hw.entity.AuthorSource;

@Component
public class AuthorProcessor implements ItemProcessor<AuthorSource, MigrationItem<Author>> {

    @Override
    public MigrationItem<Author> process(AuthorSource item) {

        Author doc = new Author();
        doc.setId(new ObjectId());
        doc.setFullName(item.getFullName());

        return new MigrationItem<>(item.getId(), doc);
    }
}
