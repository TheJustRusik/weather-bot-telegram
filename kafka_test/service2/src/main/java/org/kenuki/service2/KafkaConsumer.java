package org.kenuki.service2;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.service2.events.Bevent;
import org.kenuki.service2.events.Cevent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "Btopic", id = "group1")
public class KafkaConsumer {
    @KafkaHandler
    public void handleResultAddCityEvent(Bevent message) {
        log.info("Received B: {}:", message);
    }
    @KafkaHandler
    public void handleResultAddCityEvent(Cevent message) {
        log.info("Received C: {}:", message);
    }
}
