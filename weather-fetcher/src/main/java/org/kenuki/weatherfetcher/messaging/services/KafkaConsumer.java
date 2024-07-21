package org.kenuki.weatherfetcher.messaging.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weatherfetcher.messaging.events.AddCityEvent;
import org.kenuki.weatherfetcher.services.WeatherService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(
        topics = "add_city",
        properties = {
                "spring.json.value.default.type=org.kenuki.weatherfetcher.messaging.events.AddCityEvent",
                "spring.json.value.default.type=org.kenuki.weatherfetcher.messaging.events.ResultAddCityEvent",
        }
)
public class KafkaConsumer {
    private final WeatherService weatherService;

    @KafkaHandler
    public void addCityTransaction(AddCityEvent addCityEvent) {
        log.info("Message received, message: {}", addCityEvent);
        weatherService.createLocationWeather(addCityEvent);
    }
}
