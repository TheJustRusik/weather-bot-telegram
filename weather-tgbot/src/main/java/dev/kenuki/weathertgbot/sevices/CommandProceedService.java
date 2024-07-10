package dev.kenuki.weathertgbot.sevices;

import dev.kenuki.weathertgbot.models.entities.ChatSettings;
import dev.kenuki.weathertgbot.models.entities.Location;
import dev.kenuki.weathertgbot.repositories.ChatSettingsRepository;
import dev.kenuki.weathertgbot.telegram.TelegramBot;
import dev.kenuki.weathertgbot.utils.ChatLocalization;
import dev.kenuki.weathertgbot.utils.InlineKeyboards;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import dev.kenuki.weathertgbot.utils.CallBacksConstants;

@AllArgsConstructor
@Service
public class CommandProceedService {
    private final InlineKeyboards inlineKeyboards;
    private final TelegramClient telegramClient;
    private final ChatLocalization chatLocalization;
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
                    .text(chatLocalization.tr("hello_banner", settings.getLanguage()))
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
                .text(chatLocalization.tr("error", settings.getLanguage()))
                .build();

        switch (call_data) {
            case CallBacksConstants.setupConfiguration -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                    .text(chatLocalization.tr("your_settings", settings.getLanguage()) + "\n"
                    + chatLocalization.tr("broadcasting", settings.getLanguage()) + ": " + settings.getBroadcastWeather() + "\n"
                    + chatLocalization.tr("broadcast_time", settings.getLanguage()) + ": " + settings.getBroadcastTime() + "\n"
                    + chatLocalization.tr("utc_delta", settings.getLanguage()) + ": " + settings.getUtcDelta() + "\n"
                    + chatLocalization.tr("cities", settings.getLanguage()) + ": " + settings.getLocations().stream().map(Location::getName).toList() + "\n"
                    )
                    .build();
            case CallBacksConstants.setupLanguage -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupLanguageKeyboard(settings.getLanguage()))
                    .text(chatLocalization.tr("choose_language", settings.getLanguage()))
                    .build();
            case CallBacksConstants.setEnglish -> {
                settings.setLanguage("en");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(chatLocalization.tr("english_selected", settings.getLanguage()))
                        .build();
            }
            case CallBacksConstants.setRussian -> {
                settings.setLanguage("ru");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(chatLocalization.tr("russian_selected", settings.getLanguage()))
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
