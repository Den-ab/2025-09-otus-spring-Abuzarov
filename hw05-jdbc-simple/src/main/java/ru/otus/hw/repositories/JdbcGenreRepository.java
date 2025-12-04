package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final JdbcOperations jdbcOperations;

    public JdbcGenreRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcOperations = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public List<Genre> findAll() {
        return this.jdbcOperations.query("SELECT id, name FROM genres", new GnreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            final Genre author = this.namedParameterJdbcOperations.queryForObject(
                "SELECT id, name FROM genres WHERE id = :id",
                Map.of("id", id),
                new GnreRowMapper()
            );
            return Optional.of(author);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            final long id = rs.getLong("id");
            final String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
