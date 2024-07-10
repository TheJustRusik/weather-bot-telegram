package dev.kenuki.weathertgbot.sevices;

import dev.kenuki.weathertgbot.models.entities.ChatSettings;
import dev.kenuki.weathertgbot.models.entities.Location;
import dev.kenuki.weathertgbot.repositories.ChatSettingsRepository;
import dev.kenuki.weathertgbot.utils.InlineKeyboards;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import dev.kenuki.weathertgbot.utils.CallBacksConstants;

import static dev.kenuki.weathertgbot.utils.ChatLocalization.tr;

@AllArgsConstructor
@Service
public class CommandProceedService {
    private final InlineKeyboards inlineKeyboards;
    private final TelegramClient telegramClient;
    private final ChatSettingsRepository chatSettingsRepository;

    private final Logger log = LoggerFactory.getLogger(CommandProceedService.class);

    public void proceedCommand(Update update) {
        String msg = update.getMessage().getText();
        long chat_id = update.getMessage().getChatId();

        ChatSettings settings = chatSettingsRepository.findById(chat_id).orElseGet(() -> {
            ChatSettings chatSettings = new ChatSettings();
            chatSettings.setId(chat_id);
            chatSettingsRepository.save(chatSettings);
            return chatSettings;
        });

        if (msg.startsWith("/weather")) {
            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text(tr("hello_banner", settings.getLanguage()))
                    .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                    .replyToMessageId(update.getMessage().getMessageId())
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }



    }
    public void proceedCallback(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.getData();
        int message_id = callbackQuery.getMessage().getMessageId();
        long chat_id = callbackQuery.getMessage().getChatId();

        ChatSettings settings = chatSettingsRepository.findById(chat_id).orElseGet(() -> {
            ChatSettings chatSettings = new ChatSettings();
            chatSettings.setId(chat_id);
            chatSettingsRepository.save(chatSettings);
            return chatSettings;
        });

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chat_id)
                .replyToMessageId(message_id)
                .text(tr("error", settings.getLanguage()))
                .build();

        switch (call_data) {
            case CallBacksConstants.setupConfiguration -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                    .text(tr("your_settings", settings.getLanguage()) + "\n"
                    + tr("broadcasting", settings.getLanguage()) + ": " + settings.getBroadcastWeather() + "\n"
                    + tr("broadcast_time", settings.getLanguage()) + ": " + settings.getBroadcastTime() + "\n"
                    + tr("utc_delta", settings.getLanguage()) + ": " + settings.getUtcDelta() + "\n"
                    + tr("cities", settings.getLanguage()) + ": " + settings.getLocations().stream().map(Location::getName).toList() + "\n"
                    )
                    .build();
            case CallBacksConstants.setupLanguage -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupLanguageKeyboard(settings.getLanguage()))
                    .text(tr("choose_language", settings.getLanguage()))
                    .build();
            case CallBacksConstants.setEnglish -> {
                settings.setLanguage("en");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("english_selected", settings.getLanguage()))
                        .build();
            }
            case CallBacksConstants.setRussian -> {
                settings.setLanguage("ru");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("russian_selected", settings.getLanguage()))
                        .build();

            }
        }


        DeleteMessage deleteMessage = DeleteMessage
                .builder()
                .chatId(chat_id)
                .messageId(Math.toIntExact(message_id))
                .build();
        try {
            telegramClient.execute(deleteMessage);
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }
}
