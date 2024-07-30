package org.kenuki.weatherfetcher.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
public class ChatSettings {
    @Id
    @Column(columnDefinition = "bigint")
    private Long id;
    private Boolean broadcastWeather;
    private LocalTime broadcastTime;
    private Integer utcDelta;
    private String language;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Location> locations = new HashSet<>();


    public ChatSettings() {
        language = "en";
        broadcastWeather = false;
        broadcastTime = LocalTime.of(9,0);
        utcDelta = 5;
    }
    public ChatSettings(Long id) {
        language = "en";
        broadcastWeather = false;
        broadcastTime = LocalTime.of(9,0);
        utcDelta = 5;
        this.id = id;

    }
    public void removeLocation(final Location location) {
        locations.remove(location);
    }
    public void addLocation(final Location location) {
        locations.add(location);
    }

}
