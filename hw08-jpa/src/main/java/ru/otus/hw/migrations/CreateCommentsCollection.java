package ru.otus.hw.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@ChangeUnit(id = "v004_create_comments", order = "004", author = "denis")
public class CreateCommentsCollection {

    private final MongoDatabase mongoDatabase;

    public CreateCommentsCollection(MongoTemplate mongoTemplate) {

        this.mongoDatabase = mongoTemplate.getDb();
    }

    @Execution
    public void execute() {

        this.createComments();
    }

    @RollbackExecution
    public void rollback() {

    }

    private void createComments() {

        boolean tableExists = this.mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("comments");

        if (!tableExists) {
            Document validator = new Document(
                "$jsonSchema",
                new Document()
                    .append("bsonType", "object")
                    .append("required", List.of("content", "bookId"))
                    .append(
                        "properties",
                        new Document()
                            .append("content", new Document("bsonType", "string"))
                            .append("bookId", new Document("bsonType", "objectId"))
                    )
            );
            this.mongoDatabase.createCollection(
                "comments",
                new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator))
            );
        }
    }
}
