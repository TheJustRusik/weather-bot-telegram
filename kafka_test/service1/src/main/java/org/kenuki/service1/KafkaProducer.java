package org.kenuki.service1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.service1.events.Bevent;
import org.kenuki.service1.events.Cevent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void sendB() {
        log.info("Sending B");
        kafkaTemplate.send("Btopic", new Bevent(new Random().nextInt(), new Random().nextLong() ));
        log.info("Sending C");
        kafkaTemplate.send("Btopic", new Cevent("str", 123, 123L));
    }
}
