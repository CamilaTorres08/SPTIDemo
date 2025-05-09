package edu.eci.spti.demo.service;


import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextAnalyzerService {

    // Patrón regex para detectar URLs (http o https)
    private static final Pattern URL_PATTERN = Pattern.compile(
        "(https?://[^\\s\"'<>]+)",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    public TextAnalyzerService() {
        // Constructor
    }


    /**
     * Extrae la primera URL encontrada en el texto.
     *
     * @param text Texto de entrada que puede contener una o varias URLs.
     * @return La primera URL encontrada, o null si no se detecta ninguna.
     */
    public String extractUrl(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        Matcher matcher = URL_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


}