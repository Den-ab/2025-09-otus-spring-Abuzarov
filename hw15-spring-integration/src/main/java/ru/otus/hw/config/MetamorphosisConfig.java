package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.services.ButterflyService;
import ru.otus.hw.services.CaterpillarService;

@Configuration
@RequiredArgsConstructor
public class MetamorphosisConfig {

    private final CaterpillarService caterpillarService;

    private final ButterflyService butterflyService;

    @Bean
    public MessageChannel caterpillarChannel() {

        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow evolutionFlow() {

        return IntegrationFlow.from(caterpillarChannel())
            .log(msg -> "Начало метаморфозы для: " + msg.getPayload())
            .handle(this.caterpillarService, "transformToPupa")
            .handle(this.butterflyService, "transformToButterfly")
            .log(msg -> "Конец метаморфозы. Результат: " + msg.getPayload())
            .handle(msg -> System.out.printf("Бабочка улетела в мир: %s", msg.getPayload()))
            .get();
    }
}
