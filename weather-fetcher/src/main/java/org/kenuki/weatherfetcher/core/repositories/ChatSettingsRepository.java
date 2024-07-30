package org.kenuki.weatherfetcher.core.repositories;

import org.kenuki.weatherfetcher.core.models.entities.ChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
}
