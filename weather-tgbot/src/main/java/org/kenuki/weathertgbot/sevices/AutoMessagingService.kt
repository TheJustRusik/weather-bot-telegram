package org.kenuki.weathertgbot.sevices

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.generics.TelegramClient

@Service
class AutoMessagingService(
    private val telegramClient: TelegramClient
) {
    fun sendMessage(chatId: Long, message: String) {
        val sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(message)
            .build()

        telegramClient.execute(sendMessage)
    }
}