package org.kenuki.weatherfetcher.messaging.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weatherfetcher.messaging.events.AddCityEvent;
import org.kenuki.weatherfetcher.core.services.WeatherService;
import org.kenuki.weatherfetcher.messaging.events.TestEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "fetcher", id = "group")
public class KafkaConsumer {
    private final WeatherService weatherService;

    @KafkaHandler
    public void addCityTransaction(AddCityEvent addCityEvent) {
        log.info("Message received, message: {}", addCityEvent);
        weatherService.createLocationWeather(addCityEvent);
    }
}
