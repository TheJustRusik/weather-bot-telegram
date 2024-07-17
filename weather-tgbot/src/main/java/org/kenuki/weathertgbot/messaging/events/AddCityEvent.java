package org.kenuki.weathertgbot.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
public class AddCityEvent {
    private Long chatId;
    private String addingCity;
}
