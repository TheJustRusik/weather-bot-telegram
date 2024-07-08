package dev.kenuki.weathertgbot.sevices;

import dev.kenuki.weathertgbot.config.TelegramBotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingUpdateConsumer {
    private final TelegramClient telegramClient;
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    public TelegramBot(TelegramBotConfiguration telegramBotConfiguration) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public void consume(List<Update> list) {
        list.forEach(this::proceedUpdate);
    }

    private void proceedUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msg = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();


            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text("Chat id: " + chat_id + "❤️ Received message: " + msg)
                    .replyToMessageId(update.getMessage().getMessageId())
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
