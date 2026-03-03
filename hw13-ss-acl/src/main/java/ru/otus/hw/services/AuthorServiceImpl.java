package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    @Override
    @PostFilter("canRead(filterObject.id(), T(ru.otus.hw.models.Author))")
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream().map(this.authorConverter::convertToDTO)
            .collect(Collectors.toList());
    }
}
