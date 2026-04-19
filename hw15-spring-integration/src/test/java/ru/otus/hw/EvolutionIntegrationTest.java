package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;
import ru.otus.hw.gateway.EvolutionGateway;
import ru.otus.hw.services.ButterflyService;
import ru.otus.hw.services.CaterpillarService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EvolutionIntegrationTest {

    @Autowired
    private ButterflyService butterflyService;

    @DisplayName("Проверка того что гусеницы от 5кг превращаются в золотых бабочек.")
    @Test
    void shouldCreateGoldenButterflyWhenWeightIsHigh() {
        Pupa heavyPupa = new Pupa("Толстячок", 5.0);
        Butterfly result = butterflyService.transformToButterfly(heavyPupa);
        assertEquals("Golden", result.wingColor());
    }

    @DisplayName("Проверка того что гусеницы меньше 5кг превращаются в белых бабочек.")
    @Test
    void shouldCreateWhiteButterflyWhenWeightIsLow() {
        Pupa lightPupa = new Pupa("Худышка", 4.5);
        Butterfly result = butterflyService.transformToButterfly(lightPupa);
        assertEquals("Common White", result.wingColor());
    }
}
