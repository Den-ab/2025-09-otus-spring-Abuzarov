package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;

@Service
public class CaterpillarService {

    public Pupa transformToPupa(Caterpillar caterpillar) {

        System.out.printf("Этап 1: Гусеница %s усиленно питается.%n", caterpillar.name());

        double weight = caterpillar.appetiteLevel() * 0.5;

        return new Pupa(caterpillar.name(), weight);
    }
}
