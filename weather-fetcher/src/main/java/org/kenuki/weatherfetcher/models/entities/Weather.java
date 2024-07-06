package org.kenuki.weatherfetcher.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String country;
    private String weather;
    private String weather_description;
    private Double wind_speed;
    @Column()
    private Date date;
}
