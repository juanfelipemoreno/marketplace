package com.marketplace.mappers;

import com.marketplace.dto.usuario.*;
import com.marketplace.entities.Usuario;
import com.marketplace.enums.RolUsuario;

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
        dto.setEstado(usuario.getEstado()); // ✅ Corregido: getEstado()
        
        return dto;
    }
    
    public static Usuario toEntity(UsuarioRegistroDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(RolUsuario.COMPRADOR);
        usuario.setEstado(true);
        
        return usuario;
    }
    
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