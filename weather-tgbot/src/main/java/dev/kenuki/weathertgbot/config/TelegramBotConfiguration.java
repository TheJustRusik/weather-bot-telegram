package dev.kenuki.weathertgbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TelegramBotConfiguration {
    @Value("${telegram.bot-key}")
    private String botToken;
}
