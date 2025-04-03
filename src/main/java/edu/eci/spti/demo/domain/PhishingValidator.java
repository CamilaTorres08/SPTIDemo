package edu.eci.spti.demo.domain;

import java.util.regex.Pattern;

import java.util.regex.Pattern;

public class PhishingValidator {

    private static final Pattern SUSPICIOUS_URL_PATTERN = Pattern.compile(
            ".*(bit\\.ly|tinyurl\\.com|goog\\.gl|shady-domain\\.com).*",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean isSuspiciousUrl(String url) {
        return SUSPICIOUS_URL_PATTERN.matcher(url).matches();
    }

    public static boolean containsPhishingKeywords(String text) {
        String[] phishingKeywords = {"Urgente", "Su cuenta ha sido bloqueada", "Verifique su cuenta"};
        for (String keyword : phishingKeywords) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}


