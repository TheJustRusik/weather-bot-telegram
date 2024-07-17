package org.kenuki.weathertgbot.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultAddCityEvent extends AddCityEvent {
    private String status;
}
