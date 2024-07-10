package dev.kenuki.weathertgbot.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static dev.kenuki.weathertgbot.utils.CallBacksConstants.*;
import static dev.kenuki.weathertgbot.utils.ChatLocalization.tr;

@Component
@AllArgsConstructor
public class InlineKeyboards {

    public InlineKeyboardMarkup getMainMenuKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                            InlineKeyboardButton
                                    .builder()
                                    .text(tr("language", langCode))
                                    .callbackData(setupLanguage)
                                    .build(),
                            InlineKeyboardButton
                                    .builder()
                                    .text(tr("settings", langCode))
                                    .callbackData(setupConfiguration)
                                    .build()
                        )
                )
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(tr("information", langCode))
                                .callbackData(showInformation)
                                .build()
                        )
                )
                .build();
    }
    public InlineKeyboardMarkup getSetupLanguageKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(tr("english", langCode))
                                .callbackData(setEnglish)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(tr("russian", langCode))
                                .callbackData(setRussian)
                                .build()
                )).build();
    }
    public InlineKeyboardMarkup getSetupConfigurationKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(tr("broadcasting", langCode))
                                .callbackData(setBroadcasting)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(tr("broadcasting_time", langCode))
                                .callbackData(setBroadcastingTime)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(tr("utc_delta", langCode))
                                .callbackData(setUtcDelta)
                                .build()
                )).build();
    }
}
