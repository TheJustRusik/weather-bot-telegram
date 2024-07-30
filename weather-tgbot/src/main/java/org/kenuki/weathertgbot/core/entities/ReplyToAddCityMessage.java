package org.kenuki.weathertgbot.core.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    private ChatSettings chatSettings;

}
