package org.kenuki.weathertgbot.sevices;

import org.kenuki.weathertgbot.models.AddCityEvent;
import org.kenuki.weathertgbot.models.entities.ChatSettings;
import org.kenuki.weathertgbot.models.entities.Location;
import org.kenuki.weathertgbot.repositories.ChatSettingsRepository;
import org.kenuki.weathertgbot.repositories.LocationRepository;
import org.kenuki.weathertgbot.utils.InlineKeyboards;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kenuki.weathertgbot.utils.ChatLocalization;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


import java.util.Arrays;

import static org.kenuki.weathertgbot.utils.CallBacksConstants.*;

@AllArgsConstructor
@Service
@Slf4j
public class CommandProceedService {
    private final InlineKeyboards inlineKeyboards;
    private final TelegramClient telegramClient;
    private final ChatSettingsRepository chatSettingsRepository;
    private final LocationRepository locationRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void proceedCommand(Update update) {
        String msg = update.getMessage().getText();
        long chat_id = update.getMessage().getChatId();

        ChatSettings settings = chatSettingsRepository.findById(chat_id).orElseGet(() -> {
            ChatSettings chatSettings = new ChatSettings();
            chatSettings.setId(chat_id);
            chatSettingsRepository.save(chatSettings);
            return chatSettings;
        });

        if (msg.equals("/weather-bot") || msg.equals("/wb")) {
            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text(ChatLocalization.tr("hello_banner", settings.getLanguage()))
                    .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                    .replyToMessageId(update.getMessage().getMessageId())
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else if (msg.startsWith("/wb") && msg.split(" ").length > 1) {
            String[] subcommands = msg.split(" ");
            switch (subcommands[1]) {
                case "addcity" -> {
                    if (subcommands.length != 3)
                        return;
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chat_id)
                            .text(ChatLocalization.tr("adding_new_city", settings.getLanguage()))
                            .build();
                    DeleteMessage deleteMessage = DeleteMessage.builder()
                            .chatId(chat_id)
                            .messageId(update.getMessage().getMessageId())
                            .build();

                    log.info("Sending to kafka: {}", Arrays.stream(subcommands).toList());

                    kafkaTemplate.send("add_city", Long.toString(settings.getId()), new AddCityEvent(subcommands[2]));

                    try {
                        telegramClient.execute(sendMessage);
                        telegramClient.execute(deleteMessage);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                }
                default -> {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chat_id)
                            .text(ChatLocalization.tr("error_command", settings.getLanguage()) + subcommands[1])
                            .build();
                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                }
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
                .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                .text(ChatLocalization.tr("error", settings.getLanguage()))
                .build();

        switch (call_data) {
            case setupConfiguration -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                    .text(ChatLocalization.tr("your_settings", settings.getLanguage()) + "\n"
                    + ChatLocalization.tr("broadcasting", settings.getLanguage()) + ": " + settings.getBroadcastWeather() + "\n"
                    + ChatLocalization.tr("broadcast_time", settings.getLanguage()) + ": " + settings.getBroadcastTime() + "\n"
                    + ChatLocalization.tr("utc_delta", settings.getLanguage()) + ": " + settings.getUtcDelta() + "\n"
                    + ChatLocalization.tr("cities", settings.getLanguage()) + ": " + settings.getLocations().stream().map(Location::getName).toList() + "\n"
                    )
                    .build();
            case setupLanguage -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupLanguageKeyboard(settings.getLanguage()))
                    .text(ChatLocalization.tr("choose_language", settings.getLanguage()))
                    .build();
            case setEnglish -> {
                settings.setLanguage("en");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .text(ChatLocalization.tr("english_selected", settings.getLanguage()))
                        .build();
            }
            case setRussian -> {
                settings.setLanguage("ru");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("russian_selected", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();

            }
            case setBroadcasting -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("setup_broadcasting", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetBroadcastingKeyboard(settings.getLanguage(), settings.getBroadcastWeather()))
                        .build();
            }
            case enableBroadcasting -> {
                settings.setBroadcastWeather(true);
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("updated_broadcasting", settings.getLanguage()) + " " + ChatLocalization.tr( settings.getBroadcastWeather() ? "enabled" : "disabled", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                        .build();
            }
            case disableBroadcasting -> {
                settings.setBroadcastWeather(false);
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("updated_broadcasting", settings.getLanguage()) + " " + ChatLocalization.tr( settings.getBroadcastWeather() ? "enabled" : "disabled", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                        .build();
            }
            case showMenu -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("menu", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();
            }
            case showInformation -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("info", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();
            }
            case setLocation -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("setup_locations", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupLocationsKeyboard(settings.getLanguage(), settings.getLocations().stream().map(Location::getName).toList()))
                        .build();
            }
            case addNewLocation -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(ChatLocalization.tr("reply_for_add_city", settings.getLanguage()))
                        .build();
            }
            case exit -> {
                try {
                    telegramClient.execute(DeleteMessage.builder()
                            .chatId(chat_id)
                            .messageId(Math.toIntExact(message_id))
                            .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }

        if (call_data.startsWith(deleteLocationPrefix)) {
            String location = call_data.substring(deleteLocationPrefix.length());
            settings.removeLocation(locationRepository.findByName(location).get());//get() will always return existing location
            sendMessage = SendMessage.builder()
                            .chatId(chat_id)
                            .text(ChatLocalization.tr("deleted", settings.getLanguage()))
                            .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                            .build();
            chatSettingsRepository.save(settings);
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
