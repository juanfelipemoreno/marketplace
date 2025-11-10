package com.marketplace.rest;

import com.marketplace.dto.categoria.CategoriaResponseDTO;
import com.marketplace.dto.orden.OrdenResumenDTO;
import com.marketplace.dto.producto.ProductoResponseDTO;
import com.marketplace.dto.usuario.UsuarioResponseDTO;
import com.marketplace.enums.RolUsuario;
import com.marketplace.services.interfaces.*;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Resource para operaciones de Administrador
 * @author Felipe Moreno
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    
    @EJB
    private IUsuarioService usuarioService;
    
    @EJB
    private IProductoService productoService;
    
    @EJB
    private ICategoriaService categoriaService;
    
    @EJB
    private IOrdenService ordenService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * GET /admin/dashboard
     * Obtener estadísticas generales del sistema
     */
    @GET
    @Path("/dashboard")
    public Response obtenerDashboard() {
        validarRolAdmin();
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // Estadísticas de usuarios
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        dashboard.put("totalUsuarios", usuarios.size());
        dashboard.put("totalAdmins", usuarioService.listarPorRol(RolUsuario.ADMIN).size());
        dashboard.put("totalVendedores", usuarioService.listarPorRol(RolUsuario.VENDEDOR).size());
        dashboard.put("totalCompradores", usuarioService.listarPorRol(RolUsuario.COMPRADOR).size());
        
        // Estadísticas de productos
        dashboard.put("totalCategorias", categoriaService.listarTodas().size());
        dashboard.put("categoriasActivas", categoriaService.listarActivas().size());
        
        // Estadísticas de órdenes
        List<OrdenResumenDTO> ordenes = ordenService.listarTodas();
        dashboard.put("totalOrdenes", ordenes.size());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(dashboard);
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/usuarios
     * Listar todos los usuarios
     */
    @GET
    @Path("/usuarios")
    public Response listarUsuarios() {
        validarRolAdmin();
        
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        
        ApiResponse<List<UsuarioResponseDTO>> response = ApiResponse.success(usuarios);
        response.addMetadata("total", usuarios.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/usuarios/{id}
     * Obtener usuario por ID
     */
    @GET
    @Path("/usuarios/{id}")
    public Response obtenerUsuario(@PathParam("id") Long id) {
        validarRolAdmin();
        
        UsuarioResponseDTO usuario = usuarioService.obtenerPerfil(id);
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(usuario);
        return Response.ok(response).build();
    }
    
    /**
     * PUT /admin/usuarios/{id}/rol
     * Cambiar rol de usuario
     */
    @PUT
    @Path("/usuarios/{id}/rol")
    public Response cambiarRol(
            @PathParam("id") Long id,
            @QueryParam("rol") String nuevoRol) {
        
        validarRolAdmin();
        
        try {
            RolUsuario rol = RolUsuario.valueOf(nuevoRol.toUpperCase());
            UsuarioResponseDTO usuario = usuarioService.cambiarRol(id, rol);
            
            ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(
                "Rol actualizado exitosamente",
                usuario
            );
            
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Rol inválido. Use: ADMIN, VENDEDOR o COMPRADOR"
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
    
    /**
     * PUT /admin/usuarios/{id}/estado
     * Activar/desactivar usuario
     */
    @PUT
    @Path("/usuarios/{id}/estado")
    public Response cambiarEstadoUsuario(
            @PathParam("id") Long id,
            @QueryParam("estado") Boolean estado) {
        
        validarRolAdmin();
        
        if (estado == null) {
            ApiResponse<Object> response = ApiResponse.error("Debe especificar el estado");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        
        UsuarioResponseDTO usuario = usuarioService.cambiarEstado(id, estado);
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(
            "Estado actualizado exitosamente",
            usuario
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/categorias
     * Listar todas las categorías (incluyendo inactivas)
     */
    @GET
    @Path("/categorias")
    public Response listarCategorias() {
        validarRolAdmin();
        
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas();
        
        ApiResponse<List<CategoriaResponseDTO>> response = ApiResponse.success(categorias);
        response.addMetadata("total", categorias.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/ordenes
     * Listar todas las órdenes
     */
    @GET
    @Path("/ordenes")
    public Response listarOrdenes() {
        validarRolAdmin();
        
        List<OrdenResumenDTO> ordenes = ordenService.listarTodas();
        
        ApiResponse<List<OrdenResumenDTO>> response = ApiResponse.success(ordenes);
        response.addMetadata("total", ordenes.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/reportes/ventas
     * Reporte de ventas
     */
    @GET
    @Path("/reportes/ventas")
    public Response reporteVentas() {
        validarRolAdmin();
        
        List<OrdenResumenDTO> ordenes = ordenService.listarTodas();
        
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("totalOrdenes", ordenes.size());
        reporte.put("ordenes", ordenes);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(reporte);
        return Response.ok(response).build();
    }
    
    /**
     * GET /admin/reportes/inventario
     * Reporte de inventario
     */
    @GET
    @Path("/reportes/inventario")
    public Response reporteInventario() {
        validarRolAdmin();
        
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("totalCategorias", categoriaService.listarTodas().size());
        reporte.put("categoriasActivas", categoriaService.listarActivas().size());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(reporte);
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
            throw new WebApplicationException(
                "Acceso denegado. Se requiere rol de ADMIN",
                Response.Status.FORBIDDEN
            );
        }
    }
}