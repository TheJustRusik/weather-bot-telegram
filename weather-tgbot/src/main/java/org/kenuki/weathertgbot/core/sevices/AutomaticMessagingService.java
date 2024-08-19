package org.kenuki.weathertgbot.core.sevices;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.core.entities.ChatSettings;
import org.kenuki.weathertgbot.core.entities.Location;
import org.kenuki.weathertgbot.core.entities.Weather;
import org.kenuki.weathertgbot.core.repositories.ChatSettingsRepository;
import org.kenuki.weathertgbot.core.repositories.WeatherRepository;
import org.kenuki.weathertgbot.messaging.events.SendMessageEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.kenuki.weathertgbot.utils.ChatLocalization.tr;

@Slf4j
@Service
@AllArgsConstructor
public class AutomaticMessagingService {
    private final TelegramClient telegramClient;
    private final ChatSettingsRepository chatSettingsRepository;
    private final WeatherRepository weatherRepository;

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

    @Scheduled(cron = "0 0 * * * *")
    public void broadcastWeather() {
        LocalTime utcDate = LocalTime.now(ZoneId.of("UTC"));
        try {
            int currentHour = utcDate.getHour();

            var chats = chatSettingsRepository.findAllByBroadcastWeatherAndLocationsIsNotEmpty(true).stream().filter(
                    chat -> (currentHour + chat.getUtcDelta() + 24) % 24 == chat.getBroadcastTime().getHour()
            ).toList();

            chats.parallelStream().map(this::getWeatherMessage).forEach(weathers -> weathers.parallelStream().forEach(sendMessage -> {
                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    log.error("Failed to send message: {}", sendMessage, e);
                    // Можно добавить retry или другую логику обработки
                }
            }));
        } catch (Exception e) {
            log.error("An error occurred during weather broadcast", e);
        }
    }


    private List<SendMessage> getWeatherMessage(ChatSettings settings) {
        var cities = settings.getLocations();

        Date from = new Date();
        Date to = new Date(from.getTime() + 86_400_000);

        List<List<Weather>> cityWeathers = new ArrayList<>();

        try {
            for (Location city : cities) {
                var l = weatherRepository.findAllByCityAndDateBetween(city.getName(), from, to);
                cityWeathers.add(l);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        var messages = new ArrayList<SendMessage>(cityWeathers.stream().map(
                weathers -> SendMessage.builder()
                        .chatId(settings.getId())
                        .text(generateMessage(weathers, settings.getLanguage(), weathers.get(0).getCity(), settings.getUtcDelta()))
                        .build()
        ).toList());
        if (messages.isEmpty()) {
            messages.add(
                    SendMessage.builder()
                            .chatId(settings.getId())
                            .text(tr("no_weathers", settings.getLanguage()))
                            .build()
            );
        }
        return messages;
    }

    private @NonNull String generateMessage(List<Weather> weathers, String locale, String city, Integer timezone) {
        StringBuilder message = new StringBuilder();
        SimpleDateFormat weatherTimeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var timeZone = TimeZone.getTimeZone("GMT" + (timezone > 0 ? "+" : "") + timezone);
        dateFormat.setTimeZone(timeZone);
        weatherTimeFormat.setTimeZone(timeZone);

        message.append(tr("weather_for", locale)).append(" ").append(city).append(" ").append(tr("date", locale)).append(" ").append(dateFormat.format(new Date()));
        weathers.forEach(weather -> message.append("\n").append(weatherTimeFormat.format(weather.getDate())).append(" ").append(weather.getWeather()));
        return message.toString();
    }

}
