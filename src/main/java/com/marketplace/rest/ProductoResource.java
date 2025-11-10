package com.marketplace.rest;

import com.marketplace.dto.producto.*;
import com.marketplace.services.interfaces.IProductoService;
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
 * REST Resource para operaciones de Producto (Vendedor)
 * @author Felipe Moreno
 */
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {
    
    @EJB
    private IProductoService productoService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * POST /productos
     * Crear nuevo producto (vendedor)
     */
    @POST
    public Response crear(ProductoCreacionDTO dto) {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        ProductoResponseDTO producto = productoService.crear(dto, idVendedor);
        
        ApiResponse<ProductoResponseDTO> response = ApiResponse.success(
            "Producto creado exitosamente", 
            producto
        );
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    /**
     * PUT /productos/{id}
     * Actualizar producto (vendedor)
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, ProductoCreacionDTO dto) {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        ProductoResponseDTO producto = productoService.actualizar(id, dto, idVendedor);
        
        ApiResponse<ProductoResponseDTO> response = ApiResponse.success(
            "Producto actualizado exitosamente", 
            producto
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /productos/{id}
     * Obtener detalle de producto
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        
        ApiResponse<ProductoResponseDTO> response = ApiResponse.success(producto);
        return Response.ok(response).build();
    }
    
    /**
     * GET /productos/mis-productos
     * Listar productos del vendedor autenticado
     */
    @GET
    @Path("/mis-productos")
    public Response listarMisProductos() {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        List<ProductoResponseDTO> productos = productoService.listarMisProductos(idVendedor);
        
        ApiResponse<List<ProductoResponseDTO>> response = ApiResponse.success(productos);
        response.addMetadata("total", productos.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * DELETE /productos/{id}
     * Desactivar producto (vendedor)
     */
    @DELETE
    @Path("/{id}")
    public Response desactivar(@PathParam("id") Long id) {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        productoService.cambiarEstado(id, idVendedor, false);
        
        ApiResponse<Object> response = ApiResponse.success("Producto desactivado exitosamente");
        return Response.ok(response).build();
    }
    
    /**
     * PUT /productos/{id}/activar
     * Activar producto (vendedor)
     */
    @PUT
    @Path("/{id}/activar")
    public Response activar(@PathParam("id") Long id) {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        productoService.cambiarEstado(id, idVendedor, true);
        
        ApiResponse<Object> response = ApiResponse.success("Producto activado exitosamente");
        return Response.ok(response).build();
    }
    
    // Métodos auxiliares
    
    private Long getUsuarioAutenticado() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new WebApplicationException("Sesión no iniciada", Response.Status.UNAUTHORIZED);
        }
        
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            throw new WebApplicationException("Usuario no autenticado", Response.Status.UNAUTHORIZED);
        }
        
        return idUsuario;
    }
    
    private void validarRolVendedor() {
        HttpSession session = request.getSession(false);
        String rol = (String) session.getAttribute("rol");
        
        if (!"VENDEDOR".equals(rol) && !"ADMIN".equals(rol)) {
            throw new WebApplicationException(
                "Se requiere rol de VENDEDOR", 
                Response.Status.FORBIDDEN
            );
        }
    }
}