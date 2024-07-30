package org.kenuki.weathertgbot.messaging.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendReplyMessageEvent extends SendMessageEvent {
    protected Integer replyTo;
}
