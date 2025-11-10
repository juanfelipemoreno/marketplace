package com.marketplace.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Excepción para errores de validación
 * @author Felipe Moreno
 */
public class ValidationException extends RuntimeException {
    
    private List<String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }
    
    public ValidationException(List<String> errors) {
        super("Errores de validación: " + String.join(", ", errors));
        this.errors = errors;
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void addError(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}