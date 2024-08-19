package org.kenuki.weatherfetcher.core.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"city", "date"})
})
@IdClass(WeatherPK.class)
public class Weather {
    @Id
    private String city;
    @Id
    @Column(columnDefinition = "timestamp")
    private Date date;

    private String country;
    private String weather;
    private String weather_description;
    private Double wind_speed;

}
