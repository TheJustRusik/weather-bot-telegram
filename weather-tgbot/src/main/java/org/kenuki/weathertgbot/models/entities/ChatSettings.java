package org.kenuki.weathertgbot.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"replyToAddCityMessage", "locations"})
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

    @OneToOne(mappedBy = "chatSettings")
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private ReplyToAddCityMessage replyToAddCityMessage;

    public ChatSettings() {
        language = "en";
        broadcastWeather = false;
        broadcastTime = LocalTime.of(9,0);
        utcDelta = 5;
    }
    public void removeLocation(final Location location) {
        locations.remove(location);
    }

}