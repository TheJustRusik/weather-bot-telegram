package org.kenuki.weatherfetcher.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {
    /**
     * <b>This is city name</b>
     */
    @Id
    private String name;

}
