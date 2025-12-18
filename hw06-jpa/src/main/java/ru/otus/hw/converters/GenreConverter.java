package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(GenreDTO genre) {
        return "Id: %d, Name: %s".formatted(genre.id(), genre.name());
    }

    public GenreDTO convertToDTO(Genre genre) {

        return new GenreDTO(genre.getId(), genre.getName());
    }
}
