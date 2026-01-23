package ru.otus.hw.dto;

import java.util.List;

public record BookDTO(String id, String title, AuthorDTO author, GenreDTO genre, List<CommentDTO> comments) {}
