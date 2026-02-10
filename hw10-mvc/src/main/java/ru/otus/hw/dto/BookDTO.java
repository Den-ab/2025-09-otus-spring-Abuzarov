package ru.otus.hw.dto;

public record BookDTO(Long id, String title, AuthorDTO author, GenreDTO genre) {}
