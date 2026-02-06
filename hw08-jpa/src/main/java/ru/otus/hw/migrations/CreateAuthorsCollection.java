package ru.otus.hw.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ChangeUnit(id = "v001_create_authors", order = "001", author = "denis")
public class CreateAuthorsCollection {

    private final MongoDatabase mongoDatabase;

    public CreateAuthorsCollection(MongoTemplate mongoTemplate) {
        this.mongoDatabase = mongoTemplate.getDb();
    }

    @BeforeExecution
    public void before() {
        this.createAuthors();
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        boolean exists = mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("authors");
        if (exists) {
            mongoDatabase.getCollection("authors").drop();
        }
    }

    @Execution
    public void execute() {
        this.addAuthors();
    }

    @RollbackExecution
    public void rollback() {
        this.mongoDatabase.getCollection("authors").deleteMany(
            new Document("fullName", new Document("$in", List.of("Author_1", "Author_2", "Author_3")))
        );
    }

    private void createAuthors() {

        boolean tableExists = this.mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("authors");

        if (!tableExists) {
            Document validator = new Document(
                "$jsonSchema",
                new Document()
                    .append("bsonType", "object")
                    .append("required", List.of("fullName"))
                    .append(
                        "properties",
                        new Document().append("fullName", new Document("bsonType", "string").append("maxLength", 255))
                    )
            );
            this.mongoDatabase.createCollection(
                "authors",
                new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator))
            );
        }
    }

    private void addAuthors() {

        var authors = this.mongoDatabase.getCollection("authors");
        long existing = authors.countDocuments(
            new Document("fullName", new Document("$in", List.of("Author_1", "Author_2", "Author_3")))
        );
        if (existing == 0) {
            authors.insertMany(List.of(
                new Document("fullName", "Author_1"),
                new Document("fullName", "Author_2"),
                new Document("fullName", "Author_3")
            ));
        }
    }
}
