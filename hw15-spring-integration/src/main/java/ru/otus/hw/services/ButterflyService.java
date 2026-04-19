package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Pupa;

@Service
public class ButterflyService {

    public Butterfly transformToButterfly(Pupa pupa) {

        System.out.printf("Этап 2: Из куколки весом %.2f кг появляется бабочка.%n", pupa.weight());

        String color = pupa.weight() > 4 ? "Golden" : "Common White";

        return new Butterfly(pupa.name(), color);
    }
}
