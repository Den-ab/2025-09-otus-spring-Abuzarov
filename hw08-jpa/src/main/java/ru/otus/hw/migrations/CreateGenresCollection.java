package ru.otus.hw.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@ChangeUnit(id = "v002_create_genres", order = "002", author = "denis")
public class CreateGenresCollection {

    private final MongoDatabase mongoDatabase;

    public CreateGenresCollection(MongoTemplate mongoTemplate) {

        this.mongoDatabase = mongoTemplate.getDb();
    }

    @BeforeExecution
    public void before() {
        this.createGenres();
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        boolean exists = mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("genres");
        if (exists) {
            mongoDatabase.getCollection("genres").drop();
        }
    }
    
    @Execution
    public void execute() {
        this.addGenres();
    }
    
    @RollbackExecution
    public void rollback() {
        this.mongoDatabase.getCollection("genres").deleteMany(
            new Document("name", new Document("$in", List.of("Genre_1", "Genre_2", "Genre_3")))
        );
    }

    private void createGenres() {

        boolean tableExists = this.mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("genres");

        if (!tableExists) {
            Document validator = new Document(
                "$jsonSchema",
                new Document()
                    .append("bsonType", "object")
                    .append("required", List.of("name"))
                    .append(
                        "properties",
                        new Document().append("name", new Document("bsonType", "string").append("maxLength", 255))
                    )
            );
            this.mongoDatabase.createCollection(
                "genres",
                new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator))
            );
        }
    }

    private void addGenres() {

        var authors = this.mongoDatabase.getCollection("genres");
        long existing = authors.countDocuments(
            new Document("name", new Document("$in", List.of("Genre_1", "Genre_2", "Genre_3")))
        );
        if (existing == 0) {
            authors.insertMany(List.of(
                new Document("name", "Genre_1"),
                new Document("name", "Genre_2"),
                new Document("name", "Genre_3")
            ));
        }
    }
}
