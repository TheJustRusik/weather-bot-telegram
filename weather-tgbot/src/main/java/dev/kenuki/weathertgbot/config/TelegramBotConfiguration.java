package dev.kenuki.weathertgbot.config;

import dev.kenuki.weathertgbot.utils.ChatLocalization;
import dev.kenuki.weathertgbot.utils.InlineKeyboards;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
