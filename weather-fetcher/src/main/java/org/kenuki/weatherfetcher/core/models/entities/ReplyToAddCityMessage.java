package org.kenuki.weatherfetcher.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
