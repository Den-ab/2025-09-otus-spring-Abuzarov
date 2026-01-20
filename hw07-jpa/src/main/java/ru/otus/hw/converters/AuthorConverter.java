package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDTO author) {
        return "Id: %d, FullName: %s".formatted(author.id(), author.fullName());
    }

    public AuthorDTO convertToDTO(Author author) {

        return new AuthorDTO(author.getId(), author.getFullName());
    }
}
