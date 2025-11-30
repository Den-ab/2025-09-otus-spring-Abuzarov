package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.hw.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@RequiredArgsConstructor
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
