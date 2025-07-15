package com.raii.kafkaroundtrip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;


@Service
public class WeatherProducer {
    private static final Logger logger = LoggerFactory.getLogger(WeatherProducer.class);
    private final KafkaTemplate<Long, WeatherRecord> kafkaTemplate;

    private static final String[] CITIES = {
            "Los Angeles", "Moscow",
            "Saint Petersburg", "Seattle"
    };

    public WeatherProducer(KafkaTemplate<Long, WeatherRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void sendMessage(WeatherRecord record) {
        long timestamp = Instant.now().getEpochSecond();

        kafkaTemplate.send("weather", timestamp, record);
        logger.info("-".repeat(64));
        logger.info("Sent message: {}", record);
    }

    public void sendRandomMessage() {
        Random rnd = new Random();
        WeatherState[] weatherStates = WeatherState.values();

        WeatherRecord record = new WeatherRecord(
                CITIES[rnd.nextInt(CITIES.length)],
                rnd.nextFloat(12f, 27f),
                weatherStates[rnd.nextInt(weatherStates.length)]
        );

        sendMessage(record);
    }
}
