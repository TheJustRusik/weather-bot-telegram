package dev.kenuki.weathertgbot.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Location> locations = new HashSet<>();

    public ChatSettings() {
        language = "en";
        broadcastWeather = false;
        broadcastTime = LocalTime.of(9,0);
        utcDelta = 5;
    }

}
