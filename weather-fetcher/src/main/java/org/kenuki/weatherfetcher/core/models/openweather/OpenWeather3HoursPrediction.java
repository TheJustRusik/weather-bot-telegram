package org.kenuki.weatherfetcher.core.models.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeather3HoursPrediction {
    private List<OpenWeatherWeather> weather;
    private String dt_txt;
    private OpenWeatherWind wind;
    private Long dt;
}
