package org.kenuki.weathertgbot.core.repositories;

import org.kenuki.weathertgbot.core.entities.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
    List<ChatSettings> findAllByBroadcastWeatherAndLocationsIsNotEmpty(Boolean broadcast);
}
