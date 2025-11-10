package com.marketplace.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author Felipe Moreno
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$"
    );
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }
    
    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }
 
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }

    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.length() >= minLength;
    }

    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }

    public static boolean hasLengthInRange(String value, int minLength, int maxLength) {
        return value != null && value.length() >= minLength && value.length() <= maxLength;
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        
        // Elimina caracteres peligrosos
        return input.replaceAll("[<>\"';()&+]", "")
                   .trim();
    }
    

    public static boolean isValidFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        
        // No debe contener caracteres peligrosos ni rutas
        return !filename.contains("..") && 
               !filename.contains("/") && 
               !filename.contains("\\") &&
               filename.matches("^[a-zA-Z0-9._-]+$");
    }
}