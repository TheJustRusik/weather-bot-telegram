package org.kenuki.weatherfetcher.services;

import org.kenuki.weatherfetcher.messaging.events.AddCityEvent;
import org.kenuki.weatherfetcher.messaging.events.ResultAddCityEvent;
import org.kenuki.weatherfetcher.messaging.services.KafkaProducer;
import org.kenuki.weatherfetcher.models.OpenWeatherObject;
import org.kenuki.weatherfetcher.models.entities.Location;
import org.kenuki.weatherfetcher.models.entities.Weather;
import org.kenuki.weatherfetcher.repositories.LocationRepository;
import org.kenuki.weatherfetcher.repositories.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;


@Service
public class WeatherService {
    @Value("${open-weather.key}")
    String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final LocationRepository locationRepository;
    private final KafkaProducer kafkaProducer;

    public WeatherService(WeatherRepository weatherRepository,
                          LocationRepository locationRepository,
                          KafkaProducer kafkaProducer) {
        this.weatherRepository = weatherRepository;
        this.locationRepository = locationRepository;
        this.kafkaProducer = kafkaProducer;
    }

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
        locationRepository.findByName(addCityEvent.getAddingCity()).ifPresent((location) ->
                kafkaProducer.confirmAddingCityTransaction(
                    ResultAddCityEvent.builder()
                            .status("confirm")
                            .addingCity(addCityEvent.getAddingCity())
                            .chatId(addCityEvent.getChatId())
                            .build()
                )
        );
        try {
            fetchLocationWeather(addCityEvent.getAddingCity());
            locationRepository.save(Location.builder().name(addCityEvent.getAddingCity()).build());
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }


    }
}
