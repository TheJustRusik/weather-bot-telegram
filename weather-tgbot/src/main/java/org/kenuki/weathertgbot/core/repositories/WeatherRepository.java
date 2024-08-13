package org.kenuki.weathertgbot.core.repositories;

import org.kenuki.weathertgbot.core.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("select w from Weather w where w.city = :city and w.date = :day")
    List<Weather> findWeathersByCityAndDate(String city, Timestamp day);
}
