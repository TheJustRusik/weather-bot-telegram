package org.kenuki.weatherfetcher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class WeatherFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherFetcherApplication.class, args);

	}

}
