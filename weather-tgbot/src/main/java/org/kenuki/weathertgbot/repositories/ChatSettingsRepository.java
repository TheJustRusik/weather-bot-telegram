package org.kenuki.weathertgbot.repositories;

import org.kenuki.weathertgbot.models.entities.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {

}
