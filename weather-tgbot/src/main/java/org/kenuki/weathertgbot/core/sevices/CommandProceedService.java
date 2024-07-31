package org.kenuki.weathertgbot.core.sevices;

import org.kenuki.weathertgbot.messaging.events.AddCityEvent;
import org.kenuki.weathertgbot.core.entities.ChatSettings;
import org.kenuki.weathertgbot.core.entities.Location;
import org.kenuki.weathertgbot.core.entities.ReplyToAddCityMessage;
import org.kenuki.weathertgbot.core.repositories.ChatSettingsRepository;
import org.kenuki.weathertgbot.core.repositories.LocationRepository;
import org.kenuki.weathertgbot.utils.InlineKeyboards;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


import static org.kenuki.weathertgbot.utils.CallBacksConstants.*;
import static org.kenuki.weathertgbot.utils.ChatLocalization.tr;

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

        ChatSettings settings = null;
        try {
            settings = chatSettingsRepository.findById(chat_id).orElse(
                    chatSettingsRepository.save(new ChatSettings(chat_id))
            );
        }catch (Exception e) {
            log.error(e.getMessage());
        }


        if (msg.equals("/weather-bot") || msg.equals("/wb")) {
            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text(tr("hello_banner", settings.getLanguage()))
                    .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                    //.replyToMessageId(update.getMessage().getMessageId())
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());

            }
        } else {
            Integer replyMessageId = null;
            try {
                replyMessageId = update.getMessage().getReplyToMessage().getMessageId();
            }catch (Exception e) {
                log.error(e.getMessage());
            }

            if (replyMessageId == null || settings.getReplyToAddCityMessage() == null) {
                return;
            }
            log.info("Reply message id: {}, in db msg id: {}", replyMessageId, settings.getReplyToAddCityMessage().getMessageId());
            if (replyMessageId.equals(settings.getReplyToAddCityMessage().getMessageId())) {
                log.info("Sending to kafka: {}", msg);

                kafkaTemplate.send("fetcher", new AddCityEvent(chat_id, msg, update.getMessage().getMessageId()));
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .chatId(chat_id)
                        .messageId(replyMessageId)
                        .build();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("wait_for_add_city", settings.getLanguage()))
                        //.replyToMessageId(update.getMessage().getMessageId())
                        .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                        .build();
                try {
                    telegramClient.execute(sendMessage);
                    telegramClient.execute(deleteMessage);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());

                }
            }
        }




    }
    public void proceedCallback(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.getData();
        int message_id = callbackQuery.getMessage().getMessageId();
        long chat_id = callbackQuery.getMessage().getChatId();

        ChatSettings settings = chatSettingsRepository.findById(chat_id).orElse(
                chatSettingsRepository.save(new ChatSettings(chat_id))
        );

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chat_id)
                .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                .text(tr("error", settings.getLanguage()))
                .build();

        switch (call_data) {
            case setupConfiguration -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                    .text(tr("your_settings", settings.getLanguage()) + "\n"
                        + tr("broadcasting", settings.getLanguage()) + ": " + settings.getBroadcastWeather() + "\n"
                        + tr("broadcast_time", settings.getLanguage()) + ": " + settings.getBroadcastTime() + "\n"
                        + tr("utc_delta", settings.getLanguage()) + ": " + settings.getUtcDelta() + "\n"
                        + tr("cities", settings.getLanguage()) + ": " + settings.getLocations().stream().map(Location::getName).toList() + "\n"
                    )
                    .build();
            case setupLanguage -> sendMessage = SendMessage.builder()
                    .chatId(chat_id)
                    .replyMarkup(inlineKeyboards.getSetupLanguageKeyboard(settings.getLanguage()))
                    .text(tr("choose_language", settings.getLanguage()))
                    .build();
            case setEnglish -> {
                settings.setLanguage("en");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .text(tr("english_selected", settings.getLanguage()))
                        .build();
            }
            case setRussian -> {
                settings.setLanguage("ru");
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("russian_selected", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();

            }
            case setBroadcasting -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("setup_broadcasting", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetBroadcastingKeyboard(settings.getLanguage(), settings.getBroadcastWeather()))
                        .build();
            }
            case enableBroadcasting -> {
                settings.setBroadcastWeather(true);
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("updated_broadcasting", settings.getLanguage()) + " " + tr( settings.getBroadcastWeather() ? "enabled" : "disabled", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                        .build();
            }
            case disableBroadcasting -> {
                settings.setBroadcastWeather(false);
                chatSettingsRepository.save(settings);
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("updated_broadcasting", settings.getLanguage()) + " " + tr( settings.getBroadcastWeather() ? "enabled" : "disabled", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupConfigurationKeyboard(settings.getLanguage()))
                        .build();
            }
            case showMenu -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("menu", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();
            }
            case showInformation -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("info", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getMainMenuKeyboard(settings.getLanguage()))
                        .build();
            }
            case setLocation -> {
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("setup_locations", settings.getLanguage()))
                        .replyMarkup(inlineKeyboards.getSetupLocationsKeyboard(settings.getLanguage(), settings.getLocations().stream().map(Location::getName).toList()))
                        .build();
            }
            case addNewLocation -> {
                DeleteMessage deleteMessage = DeleteMessage
                        .builder()
                        .chatId(chat_id)
                        .messageId(Math.toIntExact(message_id))
                        .build();
                sendMessage = SendMessage.builder()
                        .chatId(chat_id)
                        .text(tr("reply_for_add_city", settings.getLanguage()))
                        .build();
                try {
                    var msgId = telegramClient.execute(sendMessage).getMessageId();
                    telegramClient.execute(deleteMessage);
                    final var replyToAddCityMessage = new ReplyToAddCityMessage(settings.getId(), msgId, settings);
                    settings.setReplyToAddCityMessage(replyToAddCityMessage);
                    chatSettingsRepository.save(settings);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
                return;

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
                            .text(tr("deleted", settings.getLanguage()))
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
