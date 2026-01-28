package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
