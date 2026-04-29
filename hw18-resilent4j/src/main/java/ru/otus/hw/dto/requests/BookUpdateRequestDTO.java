package ru.otus.hw.dto.requests;

public record BookUpdateRequestDTO(long id, String title, long genreId, long authorId) {}
