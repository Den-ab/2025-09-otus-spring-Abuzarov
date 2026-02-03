package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String findAllAuthors(Model model) {

        final List<AuthorDTO> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }
}
