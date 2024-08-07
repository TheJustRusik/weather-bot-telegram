package org.kenuki.weathertgbot.messaging.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.core.repositories.ChatSettingsRepository;
import org.kenuki.weathertgbot.core.sevices.AutomaticMessagingService;
import org.kenuki.weathertgbot.messaging.events.SendMessageEvent;
import org.kenuki.weathertgbot.utils.ChatLocalization;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static org.kenuki.weathertgbot.utils.ChatLocalization.tr;

@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "telegram_bot", id = "group2")
public class KafkaConsumer {
    private final AutomaticMessagingService automaticMessagingService;
    private final ChatSettingsRepository chatSettingsRepository;

    @KafkaHandler
    public void handleResultAddCityEvent(SendMessageEvent message) {
        log.info("Message received: {}, :", message);

        var msgParts = message.getText().split(":");
        var locale = chatSettingsRepository.findById(message.getChatId()).get().getLanguage();
        message.setText(tr(msgParts[0], locale) + " " + msgParts[1]);

        automaticMessagingService.sendOfflineMessage(message);
    }

}
