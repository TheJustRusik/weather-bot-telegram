package org.kenuki.weathertgbot.core.sevices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.messaging.events.SendMessageEvent;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@AllArgsConstructor
public class AutomaticMessagingService {
    private final TelegramClient telegramClient;

    public void sendOfflineMessage(SendMessageEvent event) {
        SendMessage sendMessage = SendMessage.builder()
                .text(event.getText())
                .chatId(event.getChatId())
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
