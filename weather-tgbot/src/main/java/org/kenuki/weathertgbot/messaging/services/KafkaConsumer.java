package org.kenuki.weathertgbot.messaging.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.messaging.events.ResultAddCityEvent;
import org.kenuki.weathertgbot.repositories.ChatSettingsRepository;
import org.kenuki.weathertgbot.repositories.LocationRepository;
import org.kenuki.weathertgbot.sevices.AutoMessagingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private final ChatSettingsRepository chatSettingsRepository;
    private final LocationRepository locationRepository;
    private final AutoMessagingService autoMessagingService;

    @KafkaListener(topics = "result_add_city", properties = {"spring.json.value.default.type=org.kenuki.weathertgbot.messaging.events.ResultAddCityEvent"})
    public void addCityTransactionResult(ResultAddCityEvent event) {
        var chatSettings = chatSettingsRepository.findById(event.getChatId()).orElseThrow();
        if (event.getStatus().equals("confirm")) {
            chatSettings.getLocations().add(locationRepository.findByName(event.getAddingCity()).orElseThrow());
            chatSettingsRepository.save(chatSettings);
            autoMessagingService.sendMessage(event.getChatId(), "added_city" + event.getAddingCity());
        } else if (event.getStatus().equals("reject")) {
            autoMessagingService.sendMessage(event.getChatId(), "city_cant_be_added" + event.getAddingCity());
        }
    }
}
