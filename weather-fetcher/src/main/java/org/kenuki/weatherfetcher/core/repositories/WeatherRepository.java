package org.kenuki.weatherfetcher.core.repositories;

import org.kenuki.weatherfetcher.core.models.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
