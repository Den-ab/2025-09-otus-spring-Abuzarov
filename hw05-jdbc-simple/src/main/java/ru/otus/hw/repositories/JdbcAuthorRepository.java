package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final JdbcOperations jdbcOperations;

    public JdbcAuthorRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcOperations = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public List<Author> findAll() {
        return this.jdbcOperations.query("SELECT id, full_name FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            final Author author = this.namedParameterJdbcOperations.queryForObject(
                "SELECT id, full_name FROM authors WHERE id = :id",
                Map.of("id", id),
                new AuthorRowMapper()
            );
            return Optional.of(author);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            final long id = rs.getLong("id");
            final String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
