package org.kenuki.weatherfetcher.messaging.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weatherfetcher.messaging.events.AddCityEvent;
import org.kenuki.weatherfetcher.messaging.events.ResultAddCityEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void confirmAddingCityTransaction(AddCityEvent addCityEvent) {
        log.info("Confirming add city transaction for chat {}", addCityEvent.getChatId());
        log.info("addCityEvent: {}", addCityEvent);
        kafkaTemplate.send(
                "add_city",
                ResultAddCityEvent.builder()
                        .status("confirm")
                        .addingCity(addCityEvent.getAddingCity())
                        .chatId(addCityEvent.getChatId())
                        .messageId(addCityEvent.getMessageId())
                        .build()
        );
    }

    public void rejectAddingCityTransaction(AddCityEvent addCityEvent) {
        log.info("Rejecting add city transaction for chat {}", addCityEvent.getChatId());
        kafkaTemplate.send(
                "add_city",
                ResultAddCityEvent.builder()
                        .status("reject")
                        .addingCity(addCityEvent.getAddingCity())
                        .chatId(addCityEvent.getChatId())
                        .messageId(addCityEvent.getMessageId())
                        .build()
        );
    }
}
