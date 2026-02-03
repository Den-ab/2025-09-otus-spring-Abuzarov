package ru.otus.hw.dto.requests;

public record BookRequestDTO(long id, String title, long genreId, long authorId) {}
