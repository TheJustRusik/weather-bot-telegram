package org.kenuki.weathertgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WeatherTgbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherTgbotApplication.class, args);
    }

}
