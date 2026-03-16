package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.document.Author;
import ru.otus.hw.document.Book;
import ru.otus.hw.document.Genre;
import ru.otus.hw.entity.AuthorSource;
import ru.otus.hw.entity.BookSource;
import ru.otus.hw.entity.GenreSource;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.processors.MigrationItem;
import ru.otus.hw.writers.AuthorWriter;
import ru.otus.hw.writers.BookWriter;
import ru.otus.hw.writers.GenreWriter;

@Configuration
@RequiredArgsConstructor
public class BatchMigrationConfig {

    private final EntityManagerFactory entityManagerFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final AuthorProcessor authorProcessor;
    
    private final GenreProcessor genreProcessor;
    
    private final BookProcessor bookProcessor;

    private final AuthorWriter authorWriter;
    
    private final GenreWriter genreWriter;
    
    private final BookWriter bookWriter;


    @Bean
    public Job migrationJob() {

        return new JobBuilder("migrationJob", this.jobRepository)
            .start(authorsStep())
            .next(genresStep())
            .next(booksStep())
            .build();
    }

    @Bean
    public Step authorsStep() {

        return new StepBuilder("authorsStep", this.jobRepository)
            .<AuthorSource, MigrationItem<Author>>chunk(50, this.transactionManager)
            .reader(authorReader())
            .processor(this.authorProcessor)
            .writer(this.authorWriter)
            .build();
    }

    @Bean
    public Step genresStep() {

        return new StepBuilder("genresStep", this.jobRepository)
            .<GenreSource, MigrationItem<Genre>>chunk(50, this.transactionManager)
            .reader(genreReader())
            .processor(this.genreProcessor)
            .writer(this.genreWriter)
            .build();
    }

    @Bean
    public Step booksStep() {

        return new StepBuilder("booksStep", this.jobRepository)
            .<BookSource, MigrationItem<Book>>chunk(50, this.transactionManager)
            .reader(bookReader())
            .processor(this.bookProcessor)
            .writer(this.bookWriter)
            .build();
    }

    @Bean
    public JpaPagingItemReader<GenreSource> genreReader() {

        JpaPagingItemReader<GenreSource> reader = new JpaPagingItemReader<>();
        reader.setName("genreReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select g from GenreSource g");
        reader.setPageSize(50);

        return reader;
    }

    @Bean
    public JpaPagingItemReader<AuthorSource> authorReader() {

        JpaPagingItemReader<AuthorSource> reader = new JpaPagingItemReader<>();
        reader.setName("authorReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from AuthorSource a");
        reader.setPageSize(50);

        return reader;
    }

    @Bean
    public JpaPagingItemReader<BookSource> bookReader() {

        JpaPagingItemReader<BookSource> reader = new JpaPagingItemReader<>();
        reader.setName("bookReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("""
            select b
            from BookSource b
            join fetch b.authorSource
            join fetch b.genreSource
        """);
        reader.setPageSize(50);

        return reader;
    }
}
