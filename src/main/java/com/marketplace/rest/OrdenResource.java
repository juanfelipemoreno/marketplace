package com.marketplace.rest;

import com.marketplace.dto.orden.*;
import com.marketplace.services.interfaces.IOrdenService;
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
 * REST Resource para operaciones de Orden
 * @author Felipe Moreno
 */
@Path("/ordenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdenResource {
    
    @EJB
    private IOrdenService ordenService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * POST /ordenes
     * Procesar compra (checkout)
     */
    @POST
    public Response procesarCompra(OrdenCreacionDTO dto) {
        Long idUsuario = getUsuarioAutenticado();
        
        OrdenResponseDTO orden = ordenService.procesarCompra(idUsuario, dto);
        
        ApiResponse<OrdenResponseDTO> response = ApiResponse.success(
            "Orden procesada exitosamente", 
            orden
        );
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    /**
     * GET /ordenes
     * Listar órdenes del usuario autenticado
     */
    @GET
    public Response listarMisOrdenes() {
        Long idUsuario = getUsuarioAutenticado();
        
        List<OrdenResumenDTO> ordenes = ordenService.listarMisOrdenes(idUsuario);
        
        ApiResponse<List<OrdenResumenDTO>> response = ApiResponse.success(ordenes);
        response.addMetadata("total", ordenes.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /ordenes/{id}
     * Ver detalle de una orden
     */
    @GET
    @Path("/{id}")
    public Response obtenerOrden(@PathParam("id") Long idOrden) {
        Long idUsuario = getUsuarioAutenticado();
        
        OrdenResponseDTO orden = ordenService.obtenerOrden(idOrden, idUsuario);
        
        ApiResponse<OrdenResponseDTO> response = ApiResponse.success(orden);
        return Response.ok(response).build();
    }
    
    /**
     * GET /ordenes/numero/{numeroOrden}
     * Buscar orden por número
     */
    @GET
    @Path("/numero/{numeroOrden}")
    public Response buscarPorNumero(@PathParam("numeroOrden") String numeroOrden) {
        Long idUsuario = getUsuarioAutenticado();
        
        OrdenResponseDTO orden = ordenService.obtenerPorNumeroOrden(numeroOrden, idUsuario);
        
        ApiResponse<OrdenResponseDTO> response = ApiResponse.success(orden);
        return Response.ok(response).build();
    }
    
    /**
     * GET /ordenes/ventas
     * Listar ventas del vendedor autenticado
     */
    @GET
    @Path("/ventas")
    public Response listarMisVentas() {
        Long idVendedor = getUsuarioAutenticado();
        validarRolVendedor();
        
        List<OrdenResumenDTO> ventas = ordenService.listarMisVentas(idVendedor);
        
        ApiResponse<List<OrdenResumenDTO>> response = ApiResponse.success(ventas);
        response.addMetadata("total", ventas.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * PUT /ordenes/{id}/cancelar
     * Cancelar orden
     */
    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarOrden(@PathParam("id") Long idOrden) {
        Long idUsuario = getUsuarioAutenticado();
        
        boolean cancelada = ordenService.cancelarOrden(idOrden, idUsuario);
        
        ApiResponse<Object> response = cancelada
            ? ApiResponse.success("Orden cancelada exitosamente")
            : ApiResponse.error("Error al cancelar la orden");
        
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