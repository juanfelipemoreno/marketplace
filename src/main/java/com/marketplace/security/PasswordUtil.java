package com.marketplace.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para encriptación y verificación de contraseñas usando BCrypt
 * @author Felipe Moreno
 */
public class PasswordUtil {
    
    private static final int WORKLOAD = 12; // Factor de trabajo para BCrypt
    
    /**
     * Encripta una contraseña usando BCrypt
     * @param passwordPlaintext Contraseña en texto plano
     * @return Hash de la contraseña
     */
    public static String hashPassword(String passwordPlaintext) {
        if (passwordPlaintext == null || passwordPlaintext.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(passwordPlaintext, salt);
    }
    
    /**
     * Verifica si una contraseña coincide con su hash
     * @param passwordPlaintext Contraseña en texto plano
     * @param storedHash Hash almacenado en la base de datos
     * @return true si la contraseña coincide
     */
    public static boolean checkPassword(String passwordPlaintext, String storedHash) {
        if (passwordPlaintext == null || storedHash == null) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(passwordPlaintext, storedHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Valida que una contraseña cumpla con los requisitos mínimos
     * - Al menos 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     * - Al menos un carácter especial
     * @param password Contraseña a validar
     * @return true si cumple con los requisitos
     */
    public static boolean validarFortaleza(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean tieneMayuscula = password.chars().anyMatch(Character::isUpperCase);
        boolean tieneMinuscula = password.chars().anyMatch(Character::isLowerCase);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);
        boolean tieneEspecial = password.chars().anyMatch(ch -> 
            "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0
        );
        
        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
    
    /**
     * Obtiene el mensaje de requisitos de contraseña
     * @return Mensaje con los requisitos
     */
    public static String obtenerMensajeRequisitos() {
        return "La contraseña debe tener al menos 8 caracteres, " +
               "incluir mayúsculas, minúsculas, números y caracteres especiales";
    }
}