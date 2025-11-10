package com.marketplace.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para respuestas estandarizadas de la API
 * @author Felipe Moreno
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }
    
    public ApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Métodos estáticos para crear respuestas comunes
    
    /**
     * Crea una respuesta exitosa con datos
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operación exitosa", data);
    }
    
    /**
     * Crea una respuesta exitosa con mensaje personalizado
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * Crea una respuesta exitosa sin datos
     */
    public static <Object> ApiResponse<Object> success(String message) {
        return new ApiResponse<>(true, message, null);
    }
    
    /**
     * Crea una respuesta de error
     */
    public static <Object> ApiResponse<Object> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    /**
     * Crea una respuesta de error con datos adicionales
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
    
    /**
     * Agrega metadatos a la respuesta
     */
    public ApiResponse<T> addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    /**
     * Agrega metadatos de paginación
     */
    public ApiResponse<T> withPagination(int page, int size, long total) {
        this.metadata.put("page", page);
        this.metadata.put("size", size);
        this.metadata.put("total", total);
        this.metadata.put("totalPages", (int) Math.ceil((double) total / size));
        return this;
    }
    
    // Getters y Setters
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}