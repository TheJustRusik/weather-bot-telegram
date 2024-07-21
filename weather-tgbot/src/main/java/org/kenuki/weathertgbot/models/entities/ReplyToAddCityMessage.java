package org.kenuki.weathertgbot.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"chatSettings"})
public class ReplyToAddCityMessage {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    private Integer messageId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private ChatSettings chatSettings;

}
