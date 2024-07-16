package org.kenuki.weathertgbot.config;

import org.kenuki.weathertgbot.utils.ChatLocalization;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Getter
@Configuration
public class TelegramBotConfiguration {
    @Value("${telegram.bot-key}")
    private String botToken;

    @Bean
    public TelegramClient getTelegramClient() {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean
    public ChatLocalization getChatLocalization() {
        return new ChatLocalization();
    }
}
