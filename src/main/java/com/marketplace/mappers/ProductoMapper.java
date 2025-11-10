package com.marketplace.mappers;

import com.marketplace.dto.producto.*;
import com.marketplace.entities.*;
import com.marketplace.enums.TipoProducto;

public class ProductoMapper {
    
    public static ProductoResponseDTO toResponseDTO(Producto producto) {
        if (producto == null) return null;
        
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setTipoProducto(producto.getTipoProducto().name());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setEstado(producto.getEstado()); // ✅ Debe ser getEstado()
        
        if (producto.getVendedor() != null) {
            dto.setIdVendedor(producto.getVendedor().getIdUsuario());
            dto.setNombreVendedor(producto.getVendedor().getNombre() + " " + 
                                 producto.getVendedor().getApellido());
        }
        
        if (producto.getCategoria() != null) {
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }
        
        if (producto.getInventarioFisico() != null) {
            int disponibilidad = producto.getInventarioFisico().getCantidadDisponible();
            dto.setDisponibilidad(disponibilidad);
            dto.setDisponible(disponibilidad > 0);
        } else if (producto.getInventarioDigital() != null) {
            int disponibilidad = producto.getInventarioDigital().getLicenciasDisponibles();
            dto.setDisponibilidad(disponibilidad);
            dto.setDisponible(disponibilidad > 0);
        }
        
        return dto;
    }
    
    public static ProductoCatalogoDTO toCatalogoDTO(Producto producto) {
        if (producto == null) return null;
        
        ProductoCatalogoDTO dto = new ProductoCatalogoDTO();
        dto.setId(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setTipoProducto(producto.getTipoProducto().name());
        dto.setImagenUrl(producto.getImagenUrl());
        
        if (producto.getCategoria() != null) {
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }
        
        if (producto.getInventarioFisico() != null) {
            int disponibilidad = producto.getInventarioFisico().getCantidadDisponible();
            dto.setDisponibilidad(disponibilidad);
            dto.setDisponible(disponibilidad > 0);
        } else if (producto.getInventarioDigital() != null) {
            int disponibilidad = producto.getInventarioDigital().getLicenciasDisponibles();
            dto.setDisponibilidad(disponibilidad);
            dto.setDisponible(disponibilidad > 0);
        }
        
        return dto;
    }
    
    public static Producto toEntity(ProductoCreacionDTO dto, Usuario vendedor, Categoria categoria) {
        if (dto == null) return null;
        
        Producto producto = new Producto();
        producto.setVendedor(vendedor);
        producto.setCategoria(categoria);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setTipoProducto(TipoProducto.valueOf(dto.getTipoProducto()));
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setEstado(true);
        
        return producto;
    }
}