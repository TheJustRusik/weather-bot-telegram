package org.kenuki.weatherfetcher.services;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weatherfetcher.models.AddCityEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class KafkaMessagingService {
    @KafkaListener(topics = "add_city", properties = {"spring.json.value.default.type=org.kenuki.weatherfetcher.models.AddCityEvent"})
    public void addCityTransaction(AddCityEvent addCityEvent) {
        log.info("Message received, message: {}", addCityEvent);
    }

}
