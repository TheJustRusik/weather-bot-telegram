package org.kenuki.weatherfetcher.repositories;

import org.kenuki.weatherfetcher.models.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
