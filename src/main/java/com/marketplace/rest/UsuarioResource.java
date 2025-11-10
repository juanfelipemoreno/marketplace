package com.marketplace.rest;

import com.marketplace.dto.usuario.*;
import com.marketplace.services.interfaces.IUsuarioService;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource para operaciones de Usuario
 * @author Felipe Moreno
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    
    @EJB
    private IUsuarioService usuarioService;
    
    @Context
    private HttpServletRequest request;
    
    /**
     * POST /usuarios/registro
     * Registrar nuevo usuario
     */
    @POST
    @Path("/registro")
    public Response registrar(UsuarioRegistroDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.registrar(dto);
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(
            "Usuario registrado exitosamente", 
            usuario
        );
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    /**
     * POST /usuarios/login
     * Iniciar sesión
     */
    @POST
    @Path("/login")
    public Response login(UsuarioLoginDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.login(dto);
        
        // Crear sesión
        HttpSession session = request.getSession(true);
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("rol", usuario.getRol());
        session.setAttribute("email", usuario.getEmail());
        session.setMaxInactiveInterval(30 * 60); // 30 minutos
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(
            "Login exitoso", 
            usuario
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * POST /usuarios/logout
     * Cerrar sesión
     */
    @POST
    @Path("/logout")
    public Response logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        ApiResponse<Object> response = ApiResponse.success("Sesión cerrada exitosamente");
        return Response.ok(response).build();
    }
    
    /**
     * GET /usuarios/perfil
     * Obtener perfil del usuario autenticado
     */
    @GET
    @Path("/perfil")
    public Response obtenerPerfil() {
        Long idUsuario = getUsuarioAutenticado();
        
        UsuarioResponseDTO usuario = usuarioService.obtenerPerfil(idUsuario);
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(usuario);
        return Response.ok(response).build();
    }
    
    /**
     * PUT /usuarios/perfil
     * Actualizar perfil
     */
    @PUT
    @Path("/perfil")
    public Response actualizarPerfil(UsuarioDTO dto) {
        Long idUsuario = getUsuarioAutenticado();
        
        UsuarioResponseDTO usuario = usuarioService.actualizarPerfil(idUsuario, dto);
        
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(
            "Perfil actualizado exitosamente", 
            usuario
        );
        
        return Response.ok(response).build();
    }
    
    /**
     * PUT /usuarios/password
     * Cambiar contraseña
     */
    @PUT
    @Path("/password")
    public Response cambiarPassword(CambioPasswordDTO dto) {
        Long idUsuario = getUsuarioAutenticado();
        
        usuarioService.cambiarPassword(idUsuario, dto);
        
        ApiResponse<Object> response = ApiResponse.success("Contraseña actualizada exitosamente");
        return Response.ok(response).build();
    }
    
    /**
     * GET /usuarios/verificar-email
     * Verificar si un email existe
     */
    @GET
    @Path("/verificar-email")
    public Response verificarEmail(@QueryParam("email") String email) {
        boolean existe = usuarioService.existeEmail(email);
        
        ApiResponse<Boolean> response = ApiResponse.success(existe);
        response.addMetadata("disponible", !existe);
        
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
    
    private String getRolUsuario() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("rol");
        }
        return null;
    }
}