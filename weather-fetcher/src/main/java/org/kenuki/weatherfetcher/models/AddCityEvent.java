package org.kenuki.weatherfetcher.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddCityEvent {
    private Long chatId;
    private String addingCity;
}
