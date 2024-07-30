package org.kenuki.weatherfetcher.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weatherfetcher.core.repositories.ChatSettingsRepository;
import org.kenuki.weatherfetcher.messaging.events.AddCityEvent;
import org.kenuki.weatherfetcher.core.models.OpenWeatherObject;
import org.kenuki.weatherfetcher.core.models.entities.Location;
import org.kenuki.weatherfetcher.core.models.entities.Weather;
import org.kenuki.weatherfetcher.core.repositories.LocationRepository;
import org.kenuki.weatherfetcher.core.repositories.WeatherRepository;
import org.kenuki.weatherfetcher.messaging.events.SendMessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${open-weather.key}")
    String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final WeatherRepository weatherRepository;
    private final LocationRepository locationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ChatSettingsRepository chatSettingsRepository;

    /**
    * <b>This method will run every day at 00:00</b>
    */
    @Scheduled(cron = "0 0 0 * * *")
    @Retryable(maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 5000))
    public void fetchWeather() {
        locationRepository.findAll().forEach(
                location -> fetchLocationWeather(location.getName())
        );
    }
    private void fetchLocationWeather(String location) {
        if (location.contains(" /&?=+_)(*&^%$#@!?/.-=")) {
            log.warn("Location have bad symbols {}", location);
            return;
        }
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="
                + location + "&appid=" + apiKey;
        log.info("Fetching weather for URL: {}", url);
        OpenWeatherObject response = restTemplate.getForObject(url, OpenWeatherObject.class);

        if(response == null) {
            log.warn("Could not fetch weather for URL: {}", url);
            throw new RuntimeException("Could not fetch weather for URL: " + url);
        }
        if(Objects.equals(response.getMessage(), "city not found")) {
            log.warn("Could not fetch weather for City: {}", location);
            throw new RuntimeException("Could not fetch weather for City: " + location);
        }

        response.getList().forEach(weather3HBlock -> {
            Weather weather = new Weather();
            weather.setCity(response.getCity().getName());
            weather.setCountry(response.getCity().getCountry());
            weather.setWeather(weather3HBlock.getWeather().get(0).getMain());
            weather.setWeather_description(weather3HBlock.getWeather().get(0).getDescription());
            weather.setWind_speed(weather3HBlock.getWind().getSpeed());
            weather.setDate(Date.from(
                    Instant.ofEpochSecond(weather3HBlock.getDt()))
            );
            weatherRepository.save(weather);
        });
    }
    public void createLocationWeather(AddCityEvent addCityEvent) {
        var locationOptional = locationRepository.findByName(addCityEvent.getAddingCity());
        var chat = chatSettingsRepository.findById(addCityEvent.getChatId()).orElseThrow();
        if (locationOptional.isPresent()) {
            chat.addLocation(locationOptional.get());
            chatSettingsRepository.save(chat);

            kafkaTemplate.send("telegram_bot", new SendMessageEvent(addCityEvent.getChatId(), "added_city:" + addCityEvent.getAddingCity()));
        } else {
            log.info("No location {} in location table.", addCityEvent.getAddingCity());
            try {
                fetchLocationWeather(addCityEvent.getAddingCity());
                var newLocation = locationRepository.save(Location.builder().name(addCityEvent.getAddingCity()).build());
                chat.addLocation(newLocation);
                chatSettingsRepository.save(chat);

                kafkaTemplate.send("telegram_bot", new SendMessageEvent(addCityEvent.getChatId(), "added_city:" + addCityEvent.getAddingCity()));
            } catch (Exception e) {
                log.warn(e.getMessage());
                kafkaTemplate.send("telegram_bot", new SendMessageEvent(addCityEvent.getChatId(), "not_added_city:" + addCityEvent.getAddingCity()));
            }
        }


    }
}
