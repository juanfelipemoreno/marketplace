package com.marketplace.exceptions;

/**
 * Excepción para stock insuficiente
 * @author Felipe Moreno
 */
public class InsufficientStockException extends BusinessException {
    
    private Long productId;
    private String productName;
    private Integer requestedQuantity;
    private Integer availableQuantity;
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(
            Long productId, 
            String productName,
            Integer requestedQuantity, 
            Integer availableQuantity) {
        
        super(String.format(
            "Stock insuficiente para el producto '%s'. " +
            "Solicitado: %d, Disponible: %d",
            productName, requestedQuantity, availableQuantity
        ));
        
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
}