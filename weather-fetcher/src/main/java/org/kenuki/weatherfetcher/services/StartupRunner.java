package org.kenuki.weatherfetcher.services;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {
    private final WeatherService weatherService;

    @Override
    public void run(String... args) throws Exception {
        weatherService.fetchWeather();
    }
}
