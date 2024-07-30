package org.kenuki.weathertgbot.core.repositories;

import org.kenuki.weathertgbot.core.entities.ReplyToAddCityMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyToAddCityMessageRepository extends JpaRepository<ReplyToAddCityMessage, Long> {

}
