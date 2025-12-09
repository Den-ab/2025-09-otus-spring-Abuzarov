package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final JdbcOperations jdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcOperations = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            final Book book = this.namedParameterJdbcOperations.queryForObject(
                "SELECT b.id, b.title, b.author_id, b.genre_id, a.full_name AS author_full_name, g.name AS genre_name "
                    + "FROM books b "
                    + "LEFT JOIN authors a ON a.id = b.author_id "
                    + "LEFT JOIN genres g ON g.id = b.genre_id "
                    + "WHERE b.id = :id ",
                Map.of("id", id),
                new BookRowMapper()
            );
            return Optional.of(book);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return this.jdbcOperations.query(
            "SELECT b.id, b.title, b.author_id, b.genre_id, a.full_name AS author_full_name, g.name AS genre_name "
                + "FROM books b "
                + "LEFT JOIN authors a ON a.id = b.author_id "
                + "LEFT JOIN genres g ON g.id = b.genre_id",
            new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        this.namedParameterJdbcOperations.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());

        this.namedParameterJdbcOperations.update(
            "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :authorId, :genreId)",
            params,
            keyHolder,
            new String[]{"id"}
        );
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        final int updateCount = this.namedParameterJdbcOperations.update(
            "UPDATE books SET title = :title, author_id = :authorId, genre_id = :genreId WHERE id = :id",
            Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId()
            )
        );

        if (updateCount == 0) {
            throw new EntityNotFoundException(String.format("Couldn't find book with id %s to update", book.getId()));
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final String title = rs.getString("title");
            final Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            final Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }
}
