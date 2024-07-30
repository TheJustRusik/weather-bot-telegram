package org.kenuki.weathertgbot.core.repositories;

import org.kenuki.weathertgbot.core.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String name);
}
