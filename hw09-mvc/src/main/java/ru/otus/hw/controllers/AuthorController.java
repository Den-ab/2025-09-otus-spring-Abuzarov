package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String findAllAuthors(Model model) {

        final List<String> authorsString = authorService.findAll().stream()
            .map(authorConverter::authorToString)
            .collect(Collectors.toList());
        model.addAttribute("authors", authorsString);
        return "authors";
    }
}
