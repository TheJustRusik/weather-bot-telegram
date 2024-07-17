package org.kenuki.weatherfetcher.messaging.events;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ResultAddCityEvent extends AddCityEvent {
    private String status;
}
