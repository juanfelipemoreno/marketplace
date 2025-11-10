package com.marketplace.exceptions;

/**
 * Excepción cuando un recurso no se encuentra
 * @author Felipe Moreno
 */
public class NotFoundException extends RuntimeException {
    
    private String resourceType;
    private Object resourceId;
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s con ID %s no encontrado", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public Object getResourceId() {
        return resourceId;
    }
}