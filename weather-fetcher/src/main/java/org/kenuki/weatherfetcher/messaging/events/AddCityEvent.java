package org.kenuki.weatherfetcher.messaging.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
public class AddCityEvent {
    private Long chatId;
    private String addingCity;
    private String messageId;
}
