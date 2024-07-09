package dev.kenuki.weathertgbot.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class ChatSettings {
    @Id
    @Column(columnDefinition = "bigint")
    private Long id;
    private Boolean broadcastWeather;
    private LocalTime broadcastTime;
    private Integer utcDelta;
    private String language;

    @ManyToMany
    @JoinTable
    private List<Location> locations;

    public ChatSettings() {
        language = "en";
        broadcastWeather = false;
        broadcastTime = LocalTime.of(9,0);
        utcDelta = 5;
    }

}
