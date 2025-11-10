package com.marketplace.config;

import com.marketplace.exceptions.*;
import com.marketplace.util.ApiResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mapper global para manejo de excepciones en la API
 * Convierte excepciones en respuestas HTTP apropiadas
 * @author Felipe Moreno
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());
    
    @Override
    public Response toResponse(Exception exception) {
        
        // Log de la excepción
        LOGGER.log(Level.SEVERE, "Excepción capturada: " + exception.getMessage(), exception);
        
        // Manejar diferentes tipos de excepciones
        if (exception instanceof NotFoundException) {
            return handleNotFoundException((NotFoundException) exception);
        }
        
        if (exception instanceof ValidationException) {
            return handleValidationException((ValidationException) exception);
        }
        
        if (exception instanceof AuthenticationException) {
            return handleAuthenticationException((AuthenticationException) exception);
        }
        
        if (exception instanceof InsufficientStockException) {
            return handleInsufficientStockException((InsufficientStockException) exception);
        }
        
        if (exception instanceof BusinessException) {
            return handleBusinessException((BusinessException) exception);
        }
        
        // Excepción genérica
        return handleGenericException(exception);
    }
    
    private Response handleNotFoundException(NotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        
        if (ex.getResourceType() != null) {
            response.addMetadata("resourceType", ex.getResourceType());
            response.addMetadata("resourceId", ex.getResourceId());
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }
    
    private Response handleValidationException(ValidationException ex) {
        ApiResponse<Object> response = ApiResponse.error("Errores de validación");
        response.addMetadata("errors", ex.getErrors());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
    
    private Response handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();
    }
    
    private Response handleInsufficientStockException(InsufficientStockException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        
        response.addMetadata("productId", ex.getProductId());
        response.addMetadata("productName", ex.getProductName());
        response.addMetadata("requested", ex.getRequestedQuantity());
        response.addMetadata("available", ex.getAvailableQuantity());
        
        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
    
    private Response handleBusinessException(BusinessException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        
        if (ex.getErrorCode() != null) {
            response.addMetadata("errorCode", ex.getErrorCode());
        }
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
    
    private Response handleGenericException(Exception ex) {
        // No exponer detalles internos en producción
        String message = "Error interno del servidor. Por favor contacte al administrador";
        
        ApiResponse<Object> response = ApiResponse.error(message);
        
        // En desarrollo, incluir más detalles
        // response.addMetadata("detail", ex.getMessage());
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }
}