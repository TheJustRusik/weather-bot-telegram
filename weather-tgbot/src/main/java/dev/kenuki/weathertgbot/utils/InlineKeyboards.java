package dev.kenuki.weathertgbot.utils;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@AllArgsConstructor
public class InlineKeyboards {
    private final ChatLocalization chatLocalization;

    public InlineKeyboardMarkup getMainMenuKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                            InlineKeyboardButton
                                    .builder()
                                    .text(chatLocalization.tr("language", langCode))
                                    .callbackData(CallBacksConstants.setupLanguage)
                                    .build(),
                            InlineKeyboardButton
                                    .builder()
                                    .text(chatLocalization.tr("settings", langCode))
                                    .callbackData(CallBacksConstants.setupConfiguration)
                                    .build()
                        )
                )
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(chatLocalization.tr("information", langCode))
                                .callbackData(CallBacksConstants.showInformation)
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
                                .text(chatLocalization.tr("english", langCode))
                                .callbackData(CallBacksConstants.setEnglish)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(chatLocalization.tr("russian", langCode))
                                .callbackData(CallBacksConstants.setRussian)
                                .build()
                )).build();
    }
    public InlineKeyboardMarkup getSetupConfigurationKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(chatLocalization.tr("broadcasting", langCode))
                                .callbackData(CallBacksConstants.setBroadcasting)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(chatLocalization.tr("broadcasting_time", langCode))
                                .callbackData(CallBacksConstants.setBroadcastingTime)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(chatLocalization.tr("utc_delta", langCode))
                                .callbackData(CallBacksConstants.setUtcDelta)
                                .build()
                )).build();
    }
}
