package org.kenuki.weathertgbot.core.repositories;

import org.kenuki.weathertgbot.core.entities.Weather;
import org.kenuki.weathertgbot.core.entities.WeatherPK;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, WeatherPK> {
    List<Weather> findAllByCityAndDateBetween(String city, Date from, Date to);
}
