package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class GenreController {

    private final GenreService genreService;

    @RequestMapping(value = "/genre", method = RequestMethod.GET)
    public ResponseEntity<List<GenreDTO>> findAllGenres(Model model) {

        final List<GenreDTO> genres = genreService.findAll();
        return ResponseEntity.ok(genres);
    }
}
