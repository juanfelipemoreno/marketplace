package com.marketplace.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

/**
 * Utilidad para generar tokens, números de orden y códigos únicos
 * @author Felipe Moreno
 */
public class TokenGenerator {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    
    /**
     * Genera un token aleatorio seguro
     * @param length Longitud del token en bytes
     * @return Token en Base64
     */
    public static String generateSecureToken(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    
    /**
     * Genera un token aleatorio seguro de 32 bytes
     * @return Token en Base64
     */
    public static String generateSecureToken() {
        return generateSecureToken(32);
    }
    
    /**
     * Genera un UUID único
     * @return UUID como String
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Genera un número de orden único
     * Formato: ORD-YYYYMMDD-XXXXXX
     * @return Número de orden
     */
    public static String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%06d", secureRandom.nextInt(1000000));
        return "ORD-" + datePart + "-" + randomPart;
    }
    
    /**
     * Genera un código alfanumérico aleatorio
     * @param length Longitud del código
     * @return Código alfanumérico
     */
    public static String generateAlphanumericCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        
        return code.toString();
    }
    
    /**
     * Genera un código numérico aleatorio
     * @param length Longitud del código
     * @return Código numérico
     */
    public static String generateNumericCode(int length) {
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            code.append(secureRandom.nextInt(10));
        }
        
        return code.toString();
    }
    
    /**
     * Genera un token de descarga temporal
     * Formato: DL-UUID
     * @return Token de descarga
     */
    public static String generateDownloadToken() {
        return "DL-" + generateUUID();
    }
    
    /**
     * Genera un nombre de archivo único
     * @param originalFilename Nombre original del archivo
     * @return Nombre único para el archivo
     */
    public static String generateUniqueFilename(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        // Extraer extensión si existe
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        
        return timestamp + "_" + uuid + extension;
    }
    
    /**
     * Genera un código de verificación de 6 dígitos
     * @return Código de verificación
     */
    public static String generateVerificationCode() {
        return generateNumericCode(6);
    }
}