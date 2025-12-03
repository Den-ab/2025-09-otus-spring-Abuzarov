package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@Controller
@RequiredArgsConstructor
@ShellComponent
public class ShellControllerImpl implements ShellController {

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService localizedIOService;

    @ShellMethod(key = "start", value = "Start test execution")
    public void home() {

        this.testRunnerService.run();
    }

    @ShellMethod(key = "about", value = "Get info about test service")
    public String about() {

        return this.localizedIOService.readStringWithPromptLocalized("TestService.about");
    }
}
