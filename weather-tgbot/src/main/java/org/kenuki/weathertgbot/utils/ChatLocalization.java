package org.kenuki.weathertgbot.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChatLocalization {
    private static Map<String, Map<String, String>> translations;

    public ChatLocalization() {
        translations = new HashMap<>();
        var res = loadTranslations();
        log.info("Translations loaded. Languages: {}, Words: {}", res.languages, res.translationAmount);
    }
    private LoadInfo loadTranslations() {
        ClassLoader classLoader = getClass().getClassLoader();

        try(InputStream inputStream = classLoader.getResourceAsStream("translations.csv")) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            CSVReader reader = new CSVReader(inputStreamReader);
            String[] headers = reader.readNext();
            if(headers == null) {
                throw new CsvValidationException("No translations were found");
            }
            while (reader.peek() != null) {
                String[] line = reader.readNext();
                if (line.length == headers.length) {
                    String key = line[0];
                    for (int i = 1; i < headers.length; i++) {
                        translations.computeIfAbsent(headers[i], k -> new HashMap<>()).put(key, line[i]);
                    }
                }
            }
        } catch (CsvValidationException | IOException e) {
            log.error("Translation loading error: {}", e.getMessage());
        }

        return LoadInfo.builder()
                .languages(translations.keySet().stream().toList())
                .translationAmount(translations.get(translations.keySet().stream().findFirst().get()).size())
                .build();
    }

    /**
     *
     * @param key code name for some phrase ex: hello_msg = Hello, Welcome to our bot!
     * @param code language code, en - english, ru - russian, etc.
     * @return if there is no "code" language, method search for en lang, if there is no translation for "key", it will return key
     */
    public static String tr(String key, String code) {
        return translations.getOrDefault(code, translations.get("en"))
                .getOrDefault(key, key);
    }

    @Builder
    private static class LoadInfo {
        private List<String> languages;
        private int translationAmount;
    }
}
