package dev.kenuki.weathertgbot.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatLocalization {
    private static Map<String, Map<String, String>> translations;

    public ChatLocalization() {
        translations = new HashMap<>();
        loadTranslations();
        System.err.println("TRANSLATION LOADED");
    }
    private void loadTranslations() {
        try(CSVReader reader = new CSVReader(new FileReader("weather-tgbot/src/main/resources/translations.csv"))) {
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
            System.err.println(e.getMessage());
        }
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
}
