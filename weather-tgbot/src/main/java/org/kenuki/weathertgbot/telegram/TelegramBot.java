package org.kenuki.weathertgbot.telegram;

import org.kenuki.weathertgbot.config.TelegramBotConfiguration;
import org.kenuki.weathertgbot.core.sevices.CommandProceedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final CommandProceedService commandProceedService;
    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    public TelegramBot(TelegramBotConfiguration telegramBotConfiguration,
                       CommandProceedService commandProceedService) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.commandProceedService = commandProceedService;
    }

    @Override
    public void consume(Update update) {
        proceedUpdate(update);
    }

    private void proceedUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandProceedService.proceedCommand(update);
        }else if (update.hasCallbackQuery()) {
            commandProceedService.proceedCallback(update.getCallbackQuery());
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
