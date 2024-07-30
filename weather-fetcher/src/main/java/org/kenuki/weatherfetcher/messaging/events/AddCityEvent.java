package org.kenuki.weatherfetcher.messaging.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCityEvent {
    private Long chatId;
    private String addingCity;
    private String messageId;
}
