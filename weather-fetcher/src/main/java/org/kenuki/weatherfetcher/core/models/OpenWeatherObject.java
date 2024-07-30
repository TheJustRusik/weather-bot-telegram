package org.kenuki.weatherfetcher.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherObject {
    private List<OpenWeather3HoursPrediction> list;
    private OpenWeatherCity city;
    private String message;
}
