package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BatchMigrationAppTests {

    @Test
    @DisplayName("Проверка что контекст загружается")
    void contextLoads() {}
}
