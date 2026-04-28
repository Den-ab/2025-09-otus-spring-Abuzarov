package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    @Override
    @CircuitBreaker(name = "dbStorage", fallbackMethod = "fallbackAuthors")
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream().map(this.authorConverter::convertToDTO).collect(Collectors.toList());
    }

    public List<AuthorDTO> fallbackAuthors(Exception e) {
        return Collections.emptyList();
    }
}
