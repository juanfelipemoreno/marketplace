package com.marketplace.rest;

import com.marketplace.dto.carrito.*;
import com.marketplace.services.interfaces.ICarritoService;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource para operaciones de Carrito
 * @author Felipe Moreno
 */
@Path("/carrito")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarritoResource {
    
    @EJB
    private ICarritoService carritoService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * GET /carrito
     * Obtener carrito del usuario autenticado
     */
    @GET
    public Response obtenerCarrito() {
        Long idUsuario = getUsuarioAutenticado();
        
        CarritoResponseDTO carrito = carritoService.obtenerCarrito(idUsuario);
        
        ApiResponse<CarritoResponseDTO> response = ApiResponse.success(carrito);
        return Response.ok(response).build();
    }
    
    /**
     * POST /carrito/items
     * Agregar producto al carrito
     */
    @POST
    @Path("/items")
    public Response agregarItem(ItemCarritoDTO dto) {
        Long idUsuario = getUsuarioAutenticado();
        
        CarritoResponseDTO carrito = carritoService.agregarItem(idUsuario, dto);
        
        ApiResponse<CarritoResponseDTO> response = ApiResponse.success(
            "Producto agregado al carrito", 
            carrito
        );
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    /**
     * PUT /carrito/items/{idItem}
     * Actualizar cantidad de un item
     */
    @PUT
    @Path("/items/{idItem}")
    public Response actualizarCantidad(
            @PathParam("idItem") Long idItem,
            @QueryParam("cantidad") Integer cantidad) {
        
        Long idUsuario = getUsuarioAutenticado();
        
        if (cantidad == null || cantidad <= 0) {
            ApiResponse<Object> response = ApiResponse.error("La cantidad debe ser mayor a 0");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        
        CarritoResponseDTO carrito = carritoService.actualizarCantidad(
            idUsuario, idItem, cantidad
        );
        
        ApiResponse<CarritoResponseDTO> response = ApiResponse.success(
            "Cantidad actualizada", 
            carrito
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * DELETE /carrito/items/{idItem}
     * Eliminar item del carrito
     */
    @DELETE
    @Path("/items/{idItem}")
    public Response eliminarItem(@PathParam("idItem") Long idItem) {
        Long idUsuario = getUsuarioAutenticado();
        
        CarritoResponseDTO carrito = carritoService.eliminarItem(idUsuario, idItem);
        
        ApiResponse<CarritoResponseDTO> response = ApiResponse.success(
            "Producto eliminado del carrito", 
            carrito
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * DELETE /carrito
     * Vaciar carrito completo
     */
    @DELETE
    public Response vaciarCarrito() {
        Long idUsuario = getUsuarioAutenticado();
        
        boolean vaciado = carritoService.vaciarCarrito(idUsuario);
        
        ApiResponse<Object> response = vaciado
            ? ApiResponse.success("Carrito vaciado exitosamente")
            : ApiResponse.error("Error al vaciar el carrito");
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /carrito/validar
     * Validar disponibilidad de items en el carrito
     */
    @GET
    @Path("/validar")
    public Response validarDisponibilidad() {
        Long idUsuario = getUsuarioAutenticado();
        
        boolean disponible = carritoService.validarDisponibilidad(idUsuario);
        
        ApiResponse<Boolean> response = ApiResponse.success(disponible);
        response.addMetadata("disponible", disponible);
        
        if (!disponible) {
            response.setMessage("Algunos productos no tienen disponibilidad suficiente");
        }
        
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
}