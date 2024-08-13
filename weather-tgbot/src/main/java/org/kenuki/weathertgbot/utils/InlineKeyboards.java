package org.kenuki.weathertgbot.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

import static org.kenuki.weathertgbot.utils.CallBacksConstants.*;
import static org.kenuki.weathertgbot.utils.ChatLocalization.tr;

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
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(tr("exit", langCode))
                                .callbackData(exit)
                                .build()
                )).build();
    }
    public InlineKeyboardMarkup getSetupLanguageKeyboard(String langCode) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(tr("kazakh", langCode))
                                .callbackData(setKazakh)
                                .build(),
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
                                .text(tr("broadcast_time", langCode))
                                .callbackData(setBroadcastingTime)
                                .build()
                )).keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(tr("utc_delta", langCode))
                                .callbackData(setUtcDelta)
                                .build(),
                        InlineKeyboardButton
                                .builder()
                                .text(tr("locations", langCode))
                                .callbackData(setLocation)
                                .build()
                )).keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(tr("show_config", langCode))
                                .callbackData(setupConfiguration)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(tr("exit", langCode))
                                .callbackData(showMenu)
                                .build()
                )).build();
    }

    public InlineKeyboardMarkup getSetBroadcastingKeyboard(String langCode, boolean currentBroadcasting) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(tr("set_enabled", langCode))
                                .callbackData(enableBroadcasting)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(tr("set_disabled", langCode))
                                .callbackData(disableBroadcasting)
                                .build()
                )).build();
    }

    public InlineKeyboardMarkup getSetupLocationsKeyboard(String langCode, List<String> locations) {
        return InlineKeyboardMarkup.builder()
                .keyboard(locations.stream().map(location -> new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(location + " ❌")
                                .callbackData(deleteLocationPrefix + location)
                                .build()
                )).toList())
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(tr("add_new_location", langCode))
                                .callbackData(addNewLocation)
                                .build()
                ))
                .build();
    }
}
