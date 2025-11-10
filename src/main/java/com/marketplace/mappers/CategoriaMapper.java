package com.marketplace.mappers;

import com.marketplace.dto.categoria.*;
import com.marketplace.entities.Categoria;

public class CategoriaMapper {
    
    public static CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        if (categoria == null) return null;
        
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setEstado(categoria.getEstado());
        
        return dto;
    }
    
    public static Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) return null;
        
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(true);
        
        return categoria;
    }
    
    public static void updateEntityFromDTO(Categoria categoria, CategoriaDTO dto) {
        if (categoria == null || dto == null) return;
        
        if (dto.getNombre() != null) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            categoria.setDescripcion(dto.getDescripcion());
        }
    }
}