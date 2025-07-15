package com.raii.kafkaroundtrip;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaRoundtripApplication implements CommandLineRunner {
    private final WeatherConsumer weatherConsumer;
    private final WeatherProducer weatherProducer;

    private final WeatherScheduler weatherScheduler;

    public KafkaRoundtripApplication(WeatherConsumer weatherConsumer,
                                     WeatherProducer weatherProducer,
                                     WeatherScheduler weatherScheduler) {
        this.weatherConsumer = weatherConsumer;
        this.weatherProducer = weatherProducer;
        this.weatherScheduler = weatherScheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaRoundtripApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
