package dev.kenuki.weathertgbot.sevices;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@AllArgsConstructor
public class ScheduledMessagingService {
    private final TelegramClient telegramClient;

    @Scheduled(cron = "0 0 * * * *")
    public void sendMessage() {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(1L)
                .text("This is broadcast message every time when second = 0 and minutes = 0 (Every hour)")
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
