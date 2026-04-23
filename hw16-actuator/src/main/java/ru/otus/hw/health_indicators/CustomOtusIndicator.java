package ru.otus.hw.health_indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;

@Component
@RequiredArgsConstructor
public class CustomOtusIndicator implements HealthIndicator {

    private final AuthorService authorService;

    @Override
    public Health health() {

        final boolean noAuthors = this.authorService.findAll().isEmpty();
        if (noAuthors) {

            return Health.down()
                .status(Status.DOWN)
                .withDetail("message", "There are no authors. This application has no point without them. See you!")
                .build();
        } else {

            return Health.up().withDetail("message", "Today is your day!").build();
        }
    }
}
