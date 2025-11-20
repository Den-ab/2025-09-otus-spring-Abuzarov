package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@ShellComponent
public class Application {

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService localizedIOService;

    public Application(TestRunnerService testRunnerService, LocalizedIOService localizedIOService) {
        this.testRunnerService = testRunnerService;
        this.localizedIOService = localizedIOService;
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @ShellMethod(key = "start", value = "Start test execution")
    public void home() {

        this.testRunnerService.run();
    }

    @ShellMethod(key = "about", value = "Get info about test service")
    public String about() {

        return this.localizedIOService.readStringWithPromptLocalized("TestService.about");
    }
}
