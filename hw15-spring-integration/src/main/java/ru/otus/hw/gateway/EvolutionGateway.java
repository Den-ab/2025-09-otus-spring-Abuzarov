package ru.otus.hw.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Caterpillar;

@MessagingGateway
public interface EvolutionGateway {

    @Gateway(requestChannel = "caterpillarChannel")
    void process(Caterpillar caterpillar);
}
