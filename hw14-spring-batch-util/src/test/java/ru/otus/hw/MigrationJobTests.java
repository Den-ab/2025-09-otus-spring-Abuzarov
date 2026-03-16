package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.document.Author;
import ru.otus.hw.document.Book;
import ru.otus.hw.document.Genre;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
public class MigrationJobTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();

        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("comments");
    }

    @Test
    @DisplayName("Проверка того что миграция произошла и в результирующей БД есть сущности со связями")
    void shouldMigrateDataFromH2ToMongoAndPreserveLinks() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        long authorsCount = mongoTemplate.getCollection("authors").countDocuments();
        long genresCount = mongoTemplate.getCollection("genres").countDocuments();
        long booksCount = mongoTemplate.getCollection("books").countDocuments();

        assertThat(authorsCount).isGreaterThan(0);
        assertThat(genresCount).isGreaterThan(0);
        assertThat(booksCount).isGreaterThan(0);

        Book book = mongoTemplate.findOne(new Query(), Book.class, "books");
        assertThat(book).isNotNull();
        assertThat(book.getAuthorId()).isNotNull();
        assertThat(book.getGenreId()).isNotNull();

        Author author = mongoTemplate.findById(book.getAuthorId(), Author.class, "authors");
        Genre genre = mongoTemplate.findById(book.getGenreId(), Genre.class, "genres");

        assertThat(author).isNotNull();
        assertThat(genre).isNotNull();
    }
}
