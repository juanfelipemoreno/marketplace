package com.marketplace.services.interfaces;

import com.marketplace.dto.usuario.*;
import com.marketplace.enums.RolUsuario;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de Usuario
 * @author Felipe Moreno
 */
public interface IUsuarioService {
    
    /**
     * Registrar nuevo usuario
     * @param dto Datos del registro
     * @return Usuario registrado
     */
    UsuarioResponseDTO registrar(UsuarioRegistroDTO dto);
    
    /**
     * Iniciar sesión
     * @param dto Credenciales de login
     * @return Usuario autenticado
     */
    UsuarioResponseDTO login(UsuarioLoginDTO dto);
    
    /**
     * Obtener perfil de usuario por ID
     * @param idUsuario ID del usuario
     * @return Perfil del usuario
     */
    UsuarioResponseDTO obtenerPerfil(Long idUsuario);
    
    /**
     * Actualizar perfil de usuario
     * @param idUsuario ID del usuario
     * @param dto Datos actualizados
     * @return Usuario actualizado
     */
    UsuarioResponseDTO actualizarPerfil(Long idUsuario, UsuarioDTO dto);
    
    /**
     * Cambiar contraseña
     * @param idUsuario ID del usuario
     * @param dto Datos del cambio de contraseña
     * @return true si se cambió exitosamente
     */
    boolean cambiarPassword(Long idUsuario, CambioPasswordDTO dto);
    
    /**
     * Listar todos los usuarios (solo admin)
     * @return Lista de usuarios
     */
    List<UsuarioResponseDTO> listarTodos();
    
    /**
     * Listar usuarios por rol
     * @param rol Rol a filtrar
     * @return Lista de usuarios
     */
    List<UsuarioResponseDTO> listarPorRol(RolUsuario rol);
    
    /**
     * Cambiar rol de usuario (solo admin)
     * @param idUsuario ID del usuario
     * @param nuevoRol Nuevo rol
     * @return Usuario actualizado
     */
    UsuarioResponseDTO cambiarRol(Long idUsuario, RolUsuario nuevoRol);
    
    /**
     * Cambiar estado de usuario (solo admin)
     * @param idUsuario ID del usuario
     * @param estado Nuevo estado
     * @return Usuario actualizado
     */
    UsuarioResponseDTO cambiarEstado(Long idUsuario, boolean estado);
    
    /**
     * Verificar si un email ya está registrado
     * @param email Email a verificar
     * @return true si existe
     */
    boolean existeEmail(String email);
}