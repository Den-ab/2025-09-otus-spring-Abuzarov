package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @Override
    @CircuitBreaker(name = "dbStorage", fallbackMethod = "fallbackGenre")
    public List<GenreDTO> findAll() {
        return genreRepository.findAll().stream()
            .map(this.genreConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<GenreDTO> fallbackGenre(Exception e) {
        return Collections.emptyList();
    }
}
