package org.kenuki.weatherfetcher.services;

import jakarta.annotation.PostConstruct;
import org.kenuki.weatherfetcher.models.OpenWeather3HoursPrediction;
import org.kenuki.weatherfetcher.models.OpenWeatherObject;
import org.kenuki.weatherfetcher.models.entities.Weather;
import org.kenuki.weatherfetcher.repositories.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


@Service
public class WeatherService {
    @Value("${open-weather.key}")
    String apiKey;
    String city = "Almaty";

    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Retryable(maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 5000))
    public void fetchWeather() {

        String url = "https://api.openweathermap.org/data/2.5/forecast?q="
                      + city + "&appid=" + apiKey;
        log.info("Fetching weather for URL: {}", url);
        OpenWeatherObject response = restTemplate.getForObject(url, OpenWeatherObject.class);

        if(response == null) {
            log.warn("Could not fetch weather for URL: {}", url);
            throw new RuntimeException("Could not fetch weather for URL: " + url);
        }

        response.getList().forEach(weather3HBlock -> {
            Weather weather = new Weather();
            weather.setCity(response.getCity().getName());
            weather.setCountry(response.getCity().getCountry());
            weather.setWeather(weather3HBlock.getWeather().get(0).getMain());
            weather.setWeather_description(weather3HBlock.getWeather().get(0).getDescription());
            weather.setWind_speed(weather3HBlock.getWind().getSpeed());
            weather.setDate(new Date(weather3HBlock.getDt()));
            weatherRepository.save(weather);
        });
    }
}
