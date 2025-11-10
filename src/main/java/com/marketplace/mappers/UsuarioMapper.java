/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marketplace.mappers;

import com.marketplace.dto.usuario.*;
import com.marketplace.entities.Usuario;
import com.marketplace.enums.RolUsuario;
/**
 *
 * @author Felipe Moreno
 */
public class UsuarioMapper {
   public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().name());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setEstado(usuario.isEstado());
        
        return dto;
    }
    
    /**
     * Convierte Entity Usuario a UsuarioDTO
     */
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setEstado(usuario.isEstado());
        
        return dto;
    }
    
    /**
     * Convierte UsuarioRegistroDTO a Entity Usuario
     * NO incluye el hash de contraseña (debe hacerse en el servicio)
     */
    public static Usuario toEntity(UsuarioRegistroDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        // El password se maneja en el servicio con BCrypt
        usuario.setRol(RolUsuario.COMPRADOR); // Rol por defecto
        usuario.setEstado(true);
        
        return usuario;
    }
    
    /**
     * Actualiza un Entity Usuario con datos de UsuarioDTO
     * Solo actualiza campos editables por el usuario
     */
    public static void updateEntityFromDTO(Usuario usuario, UsuarioDTO dto) {
        if (usuario == null || dto == null) return;
        
        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

    }
}
