package org.kenuki.weatherfetcher.repositories;

import org.kenuki.weatherfetcher.models.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> {

}
