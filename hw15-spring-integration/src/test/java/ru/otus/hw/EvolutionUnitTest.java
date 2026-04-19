package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;
import ru.otus.hw.gateway.EvolutionGateway;
import ru.otus.hw.services.ButterflyService;
import ru.otus.hw.services.CaterpillarService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EvolutionUnitTest {

    @Autowired
    private EvolutionGateway gateway;

    @MockitoBean
    private ButterflyService butterflyService;

    @MockitoBean
    private CaterpillarService caterpillarService;

    @DisplayName("Проверка того что вызывается метод трансформации в куколку.")
    @Test
    void testCaterpillarFlow() {
        Caterpillar caterpillar = new Caterpillar("Тестовая гусеница", 5);
        gateway.process(caterpillar);
        verify(this.caterpillarService, atLeastOnce()).transformToPupa(any());
    }

    @DisplayName("Проверка того что вызывается метод трансформации в бабочку.")
    @Test
    void testButterflyFlow() {

        when(caterpillarService.transformToPupa(any())).thenReturn(new Pupa("Тестовая гусеница", 5.0));

        Caterpillar caterpillar = new Caterpillar("Тестовая гусеница", 5);
        gateway.process(caterpillar);
        verify(this.butterflyService, atLeastOnce()).transformToButterfly(any());
    }
}
