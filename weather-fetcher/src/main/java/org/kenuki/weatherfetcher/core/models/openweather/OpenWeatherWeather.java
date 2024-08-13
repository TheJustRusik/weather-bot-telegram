package org.kenuki.weatherfetcher.core.models.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherWeather {
    private String main;
    private String description;
}
