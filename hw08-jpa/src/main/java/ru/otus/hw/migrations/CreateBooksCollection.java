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
import java.util.Objects;
import java.util.stream.Stream;

@ChangeUnit(id = "v003_create_books", order = "003", author = "denis")
public class CreateBooksCollection {

    private final MongoDatabase mongoDatabase;

    public CreateBooksCollection(MongoTemplate mongoTemplate) {
        this.mongoDatabase = mongoTemplate.getDb();
    }

    @BeforeExecution
    public void before() {
        this.createBooks();
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        boolean exists = mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("books");
        if (exists) {
            mongoDatabase.getCollection("books").drop();
        }
    }

    @Execution
    public void execute() {
        this.addBooks();
    }

    @RollbackExecution
    public void rollback() {
        this.mongoDatabase.getCollection("books").deleteMany(
            new Document("title", new Document("$in", List.of("Book_1", "Book_2", "Book_3")))
        );
    }

    private void createBooks() {

        boolean tableExists = this.mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains("books");

        if (!tableExists) {
            Document validator = this.buildBookDocument();

            this.mongoDatabase.createCollection(
                "books",
                new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator))
            );
            this.mongoDatabase.getCollection("books").createIndex(new Document("authorId", 1));
            this.mongoDatabase.getCollection("books").createIndex(new Document("genreId", 1));
        }
    }

    private void addBooks() {

        var books = this.mongoDatabase.getCollection("books");
        long existingBooks = books.countDocuments(
            new Document("title", new Document("$in", List.of("Book_1", "Book_2", "Book_3")))
        );
        if (existingBooks > 0) {
            return;
        }

        final List<Document> booksToInsert = this.buildBooksDataCollection();
        books.insertMany(booksToInsert);
    }

    private Document buildBookDocument() {
        return new Document(
            "$jsonSchema",
            new Document()
                .append("bsonType", "object")
                .append("required", List.of("title", "authorId", "genreId", "comments"))
                .append("properties", new Document()
                    .append("title", new Document("bsonType", "string").append("maxLength", 255))
                    .append("authorId", new Document("bsonType", "objectId"))
                    .append("genreId", new Document("bsonType", "objectId"))
                    .append("comments", new Document()
                        .append("bsonType", "array")
                        .append("items", new Document()
                            .append("bsonType", "object")
                            .append("required", List.of("_id"))
                            .append("properties", new Document().append("_id", new Document("bsonType", "objectId")))
                        )
                    )
                )
        );
    }

    private List<Document> buildBooksDataCollection() {
        var authors = this.mongoDatabase.getCollection("authors");
        var genres = this.mongoDatabase.getCollection("genres");
        var a1 = authors.find(new Document("fullName", "Author_1")).first();
        var a2 = authors.find(new Document("fullName", "Author_2")).first();
        var a3 = authors.find(new Document("fullName", "Author_3")).first();
        var g1 = genres.find(new Document("name", "Genre_1")).first();
        var g2 = genres.find(new Document("name", "Genre_2")).first();
        var g3 = genres.find(new Document("name", "Genre_3")).first();

        final boolean someEmpty = Stream.of(a1, a2, a3, g1, g2, g3).anyMatch(Objects::isNull);
        if (someEmpty) {
            throw new IllegalStateException("Authors/genres not found. Run authors+genres migrations before books.");
        }
        var b1 = new Document("title", "Book_1").append("authorId", a1.getObjectId("_id"))
            .append("genreId", g1.getObjectId("_id"))
            .append("comments", List.of());
        var b2 = new Document("title", "Book_2").append("authorId", a2.getObjectId("_id"))
            .append("genreId", g2.getObjectId("_id"))
            .append("comments", List.of());
        var b3 = new Document("title", "Book_3").append("authorId", a3.getObjectId("_id"))
            .append("genreId", g3.getObjectId("_id"))
            .append("comments", List.of());
        return List.of(b1, b2, b3);
    }
}
