package com.raii.kafkaroundtrip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class WeatherAnalytics {
    private Integer sunnyDays;
    private Integer rainyDays;

    public WeatherAnalytics(Integer sunnyDays, Integer rainyDays) {
        this.sunnyDays = sunnyDays;
        this.rainyDays = rainyDays;
    }

    public Integer getSunnyDays() {
        return sunnyDays;
    }

    public Integer getRainyDays() {
        return rainyDays;
    }

    public void incrementSunnyDays() {
        this.sunnyDays += 1;
    }

    public void incrementRainyDays() {
        this.rainyDays += 1;
    }
}

@Service
public class WeatherConsumer {
    private static final Logger logger = LoggerFactory.getLogger(WeatherConsumer.class);
    private static final Integer BATCH_SIZE = 5;

    private final BlockingQueue<WeatherRecord> weatherQueue = new LinkedBlockingQueue<>(BATCH_SIZE);
    private final Map<String, WeatherAnalytics> cityAnalyticsMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "weather", groupId = "weather-all")
    public void consume(WeatherRecord record) {
        logger.info("Received message: {}", record);
        weatherQueue.add(record);

        // O(1)
        if (weatherQueue.size() >= BATCH_SIZE) {
            performAnalytics();
        }
    }

    private void performAnalytics() {
        while (!weatherQueue.isEmpty()) {
            WeatherRecord record = weatherQueue.poll();
            WeatherAnalytics analytics = cityAnalyticsMap.getOrDefault(record.city(), new WeatherAnalytics(0, 0));

            switch (record.state()) {
                case Sunny -> analytics.incrementSunnyDays();
                case Rain -> analytics.incrementRainyDays();
            }

            cityAnalyticsMap.put(record.city(), analytics);
        }

        // NOTE! it would be better to implement the sorting functionality
        //       in a separate method of the `WeatherAnalytics` class that
        //       accepts the predicate.

        Map<String, Integer> sunnyDaysMap = cityAnalyticsMap.entrySet().stream()
                .sorted(Comparator.comparing(
                        (Map.Entry<String, WeatherAnalytics> entry) -> entry.getValue().getSunnyDays(),
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getSunnyDays(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        Map<String, Integer> rainyDaysMap = cityAnalyticsMap.entrySet().stream()
                .sorted(Comparator.comparing(
                        (Map.Entry<String, WeatherAnalytics> entry) -> entry.getValue().getRainyDays(),
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getRainyDays(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        logger.info(">>> Weather Statistics");
        logger.info(" * Sunny days by city: {}", sunnyDaysMap);
        logger.info(" * Rainy days by city: {}", rainyDaysMap);
    }
}
