package dev.kenuki.weathertgbot.repositories;

import dev.kenuki.weathertgbot.models.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
