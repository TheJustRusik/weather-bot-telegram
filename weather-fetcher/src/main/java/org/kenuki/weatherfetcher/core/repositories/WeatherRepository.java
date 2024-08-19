package org.kenuki.weatherfetcher.core.repositories;

import org.kenuki.weatherfetcher.core.models.entities.Weather;
import org.kenuki.weatherfetcher.core.models.entities.WeatherPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface WeatherRepository extends JpaRepository<Weather, WeatherPK> {
    void deleteAllByDateBefore(Date date);
}
