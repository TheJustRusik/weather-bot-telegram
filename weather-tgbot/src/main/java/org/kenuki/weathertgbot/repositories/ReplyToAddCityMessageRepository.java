package org.kenuki.weathertgbot.repositories;

import org.kenuki.weathertgbot.models.entities.ChatSettings;
import org.kenuki.weathertgbot.models.entities.ReplyToAddCityMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReplyToAddCityMessageRepository extends JpaRepository<ReplyToAddCityMessage, Long> {

}
