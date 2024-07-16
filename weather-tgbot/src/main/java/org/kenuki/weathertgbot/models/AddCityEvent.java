package org.kenuki.weathertgbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddCityEvent {
    private Long chatId;
    private String addingCity;
}
