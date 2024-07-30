package org.kenuki.weatherfetcher.messaging.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class SendReplyMessageEvent extends SendMessageEvent {
    protected Integer replyTo;
}
