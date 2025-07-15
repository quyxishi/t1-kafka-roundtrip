package com.raii.kafkaroundtrip;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherScheduler {
    private final WeatherProducer weatherProducer;

    public WeatherScheduler(WeatherProducer weatherProducer) {
        this.weatherProducer = weatherProducer;
    }

    @Scheduled(fixedRate = 2000)
    public void sendWeatherMessage() {
        weatherProducer.sendRandomMessage();
    }
}
