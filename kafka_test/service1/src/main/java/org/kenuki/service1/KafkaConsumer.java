package org.kenuki.service1;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.service1.events.Aevent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "Atopic", id = "group2")
public class KafkaConsumer {
    @KafkaHandler
    public void handleResultAddCityEvent(Aevent message) {
        log.info("Recieved A: {}:", message);
    }
}
