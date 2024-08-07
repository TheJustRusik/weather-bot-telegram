package org.kenuki.weatherfetcher.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherCity {
    private String name;
    private String country;
}