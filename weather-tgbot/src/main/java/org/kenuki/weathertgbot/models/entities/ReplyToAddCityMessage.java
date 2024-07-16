package org.kenuki.weathertgbot.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyToAddCityMessage {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    private Integer messageId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "chat_id")
    private ChatSettings chatSettings;

}
