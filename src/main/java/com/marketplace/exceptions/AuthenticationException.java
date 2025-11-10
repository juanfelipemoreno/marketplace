package com.marketplace.exceptions;

/**
 * Excepción para errores de autenticación
 * @author Felipe Moreno
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Crea una excepción para credenciales inválidas
     * @return AuthenticationException
     */
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Email o contraseña incorrectos");
    }
    
    /**
     * Crea una excepción para usuario inactivo
     * @return AuthenticationException
     */
    public static AuthenticationException inactiveUser() {
        return new AuthenticationException("Usuario inactivo. Contacte al administrador");
    }
    
    /**
     * Crea una excepción para sesión expirada
     * @return AuthenticationException
     */
    public static AuthenticationException sessionExpired() {
        return new AuthenticationException("Sesión expirada. Por favor inicie sesión nuevamente");
    }
    
    /**
     * Crea una excepción para token inválido
     * @return AuthenticationException
     */
    public static AuthenticationException invalidToken() {
        return new AuthenticationException("Token de autenticación inválido");
    }
    
    /**
     * Crea una excepción para acceso no autorizado
     * @return AuthenticationException
     */
    public static AuthenticationException unauthorized() {
        return new AuthenticationException("No tiene autorización para acceder a este recurso");
    }
}