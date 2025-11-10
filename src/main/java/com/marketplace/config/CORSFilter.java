package com.marketplace.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filtro para habilitar CORS (Cross-Origin Resource Sharing)
 * Permite que el frontend pueda hacer peticiones al backend desde diferentes dominios
 * @author Felipe Moreno
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
    
    @Override
    public void filter(ContainerRequestContext requestContext, 
                      ContainerResponseContext responseContext) throws IOException {
        
        // Permite peticiones desde cualquier origen
        // En producción, especificar el dominio exacto del frontend
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        
        // Métodos HTTP permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        
        // Headers permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        
        // Permite credenciales (cookies, headers de autenticación)
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        
        // Tiempo máximo de cache para preflight requests (24 horas)
        responseContext.getHeaders().add("Access-Control-Max-Age", "86400");
        
        // Headers que el cliente puede leer
        responseContext.getHeaders().add("Access-Control-Expose-Headers",
            "Content-Type, Authorization, X-Total-Count");
    }
}