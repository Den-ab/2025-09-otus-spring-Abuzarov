package ru.otus.hw.dto.requests;

public record BookCreateRequestDTO(String title, long genreId, long authorId) {}
