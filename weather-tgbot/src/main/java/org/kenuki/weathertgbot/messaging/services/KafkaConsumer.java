package org.kenuki.weathertgbot.messaging.services;

import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.messaging.events.AddCityEvent;
import org.kenuki.weathertgbot.messaging.events.SendMessageEvent;
import org.kenuki.weathertgbot.messaging.events.SendReplyMessageEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@KafkaListener(topics = "telegram_bot", id = "plan")
public class KafkaConsumer {
    @KafkaHandler
    public void handleResultAddCityEvent(SendMessageEvent message) {
        log.info("Recieved resultAddCityEvent: {}, :", message);
    }
    @KafkaHandler
    public void handleResultAddCityEvent(SendReplyMessageEvent message) {
        log.info("Recieved resultAddCityEvent: {}, header:", message);
    }
}
