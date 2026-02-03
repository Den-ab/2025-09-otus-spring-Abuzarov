package ru.otus.hw.dto;

public record BookRequestDTO(long id, String title, long genreId, long authorId) {}
