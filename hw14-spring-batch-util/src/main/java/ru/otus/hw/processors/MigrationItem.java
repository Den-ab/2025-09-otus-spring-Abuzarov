package ru.otus.hw.processors;

public record MigrationItem<T>(Long sourceId, T document) {}
