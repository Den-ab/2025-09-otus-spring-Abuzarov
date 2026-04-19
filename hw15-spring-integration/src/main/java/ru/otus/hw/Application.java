package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.gateway.EvolutionGateway;

import java.util.Random;

@SpringBootApplication
@EnableIntegration
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(EvolutionGateway gateway) {

        return args -> {

            Random random = new Random();

            for (int i = 1; i < 6; i++) {

                Caterpillar caterpillar = new Caterpillar("Гусеница-" + i, Math.abs(random.nextInt(1, 11)));
                System.out.println("\n --- ЗАПУСК МЕТАМОРФОЗЫ ---");
                gateway.process(caterpillar);
                System.out.println("\n --- СООБЩЕНИЕ ОТПРАВЛЕНО В ПОТОК ---");
            }
        };
    }
}
