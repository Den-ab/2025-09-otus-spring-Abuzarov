package ru.otus.hw.dto;

public record BookDTO(String id, String title, AuthorDTO author, GenreDTO genre) {}
