package com.marketplace.rest;

import com.marketplace.dto.orden.DescargaResponseDTO;
import com.marketplace.services.interfaces.IDescargaService;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * REST Resource para operaciones de Descarga
 * @author Felipe Moreno
 */
@Path("/descargas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DescargaResource {
    
    @EJB
    private IDescargaService descargaService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * GET /descargas
     * Listar productos digitales disponibles para descarga
     */
    @GET
    public Response listarDescargas() {
        Long idUsuario = getUsuarioAutenticado();
        
        List<DescargaResponseDTO> descargas = descargaService.listarDescargasDisponibles(idUsuario);
        
        ApiResponse<List<DescargaResponseDTO>> response = ApiResponse.success(descargas);
        response.addMetadata("total", descargas.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /descargas/todas
     * Listar todas las descargas del usuario (incluyendo las que alcanzaron el límite)
     */
    @GET
    @Path("/todas")
    public Response listarTodasLasDescargas() {
        Long idUsuario = getUsuarioAutenticado();
        
        List<DescargaResponseDTO> descargas = descargaService.listarMisDescargas(idUsuario);
        
        ApiResponse<List<DescargaResponseDTO>> response = ApiResponse.success(descargas);
        response.addMetadata("total", descargas.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /descargas/{idProducto}/info
     * Obtener información de descarga de un producto
     */
    @GET
    @Path("/{idProducto}/info")
    public Response obtenerInfo(@PathParam("idProducto") Long idProducto) {
        Long idUsuario = getUsuarioAutenticado();
        
        DescargaResponseDTO descarga = descargaService.obtenerInformacionDescarga(
            idUsuario, idProducto
        );
        
        ApiResponse<DescargaResponseDTO> response = ApiResponse.success(descarga);
        return Response.ok(response).build();
    }
    
    /**
     * GET /descargas/{idProducto}/archivo
     * Descargar archivo (redirige a la URL del archivo)
     */
    @GET
    @Path("/{idProducto}/archivo")
    public Response descargarArchivo(@PathParam("idProducto") Long idProducto) {
        Long idUsuario = getUsuarioAutenticado();
        
        // Verificar si puede descargar
        if (!descargaService.puedeDescargar(idUsuario, idProducto)) {
            ApiResponse<Object> response = ApiResponse.error(
                "Ha alcanzado el límite de descargas para este producto"
            );
            return Response.status(Response.Status.FORBIDDEN).entity(response).build();
        }
        
        // Registrar la descarga (incrementar contador)
        descargaService.registrarDescarga(idUsuario, idProducto);
        
        // Obtener URL del archivo
        String archivoUrl = descargaService.obtenerUrlDescarga(idUsuario, idProducto);
        
        // Redirigir a la URL del archivo
        // En un escenario real, aquí se generaría un token temporal de descarga
        return Response.seeOther(URI.create(archivoUrl)).build();
    }
    
    /**
     * POST /descargas/{idProducto}/registrar
     * Registrar una descarga (incrementar contador)
     */
    @POST
    @Path("/{idProducto}/registrar")
    public Response registrarDescarga(@PathParam("idProducto") Long idProducto) {
        Long idUsuario = getUsuarioAutenticado();
        
        DescargaResponseDTO descarga = descargaService.registrarDescarga(idUsuario, idProducto);
        
        ApiResponse<DescargaResponseDTO> response = ApiResponse.success(
            "Descarga registrada exitosamente", 
            descarga
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /descargas/{idProducto}/puede-descargar
     * Verificar si puede descargar un producto
     */
    @GET
    @Path("/{idProducto}/puede-descargar")
    public Response puedeDescargar(@PathParam("idProducto") Long idProducto) {
        Long idUsuario = getUsuarioAutenticado();
        
        boolean puede = descargaService.puedeDescargar(idUsuario, idProducto);
        
        ApiResponse<Boolean> response = ApiResponse.success(puede);
        response.addMetadata("puedeDescargar", puede);
        
        if (!puede) {
            response.setMessage("No puede descargar este producto");
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