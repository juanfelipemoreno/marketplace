package com.marketplace.rest;

import com.marketplace.dto.categoria.*;
import com.marketplace.services.interfaces.ICategoriaService;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource para operaciones de Categoría
 * @author Felipe Moreno
 */
@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {
    
    @EJB
    private ICategoriaService categoriaService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * GET /categorias
     * Listar categorías activas
     */
    @GET
    public Response listarActivas() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarActivas();
        
        ApiResponse<List<CategoriaResponseDTO>> response = ApiResponse.success(categorias);
        return Response.ok(response).build();
    }
    
    /**
     * GET /categorias/{id}
     * Obtener categoría por ID
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        CategoriaResponseDTO categoria = categoriaService.obtenerPorId(id);
        
        ApiResponse<CategoriaResponseDTO> response = ApiResponse.success(categoria);
        return Response.ok(response).build();
    }
    
    /**
     * POST /categorias
     * Crear categoría (solo admin)
     */
    @POST
    public Response crear(CategoriaDTO dto) {
        validarRolAdmin();
        
        CategoriaResponseDTO categoria = categoriaService.crear(dto);
        
        ApiResponse<CategoriaResponseDTO> response = ApiResponse.success(
            "Categoría creada exitosamente", 
            categoria
        );
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    /**
     * PUT /categorias/{id}
     * Actualizar categoría (solo admin)
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, CategoriaDTO dto) {
        validarRolAdmin();
        
        CategoriaResponseDTO categoria = categoriaService.actualizar(id, dto);
        
        ApiResponse<CategoriaResponseDTO> response = ApiResponse.success(
            "Categoría actualizada exitosamente", 
            categoria
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * DELETE /categorias/{id}
     * Desactivar categoría (solo admin)
     */
    @DELETE
    @Path("/{id}")
    public Response desactivar(@PathParam("id") Long id) {
        validarRolAdmin();
        
        categoriaService.cambiarEstado(id, false);
        
        ApiResponse<Object> response = ApiResponse.success("Categoría desactivada exitosamente");
        return Response.ok(response).build();
    }
    
    /**
     * GET /categorias/todas
     * Listar todas las categorías (solo admin)
     */
    @GET
    @Path("/todas")
    public Response listarTodas() {
        validarRolAdmin();
        
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas();
        
        ApiResponse<List<CategoriaResponseDTO>> response = ApiResponse.success(categorias);
        return Response.ok(response).build();
    }
    
    // Métodos auxiliares
    
    private void validarRolAdmin() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new WebApplicationException("Sesión no iniciada", Response.Status.UNAUTHORIZED);
        }
        
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            throw new WebApplicationException("Acceso denegado", Response.Status.FORBIDDEN);
        }
    }
}