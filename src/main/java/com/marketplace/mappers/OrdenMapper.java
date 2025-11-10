package com.marketplace.mappers;

import com.marketplace.dto.orden.*;
import com.marketplace.entities.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrdenMapper {
    
    /**
     * Convierte Entity Orden a OrdenResponseDTO
     */
    public static OrdenResponseDTO toResponseDTO(Orden orden) {
        if (orden == null) return null;
        
        OrdenResponseDTO dto = new OrdenResponseDTO();
        dto.setId(orden.getIdOrden());
        dto.setNumeroOrden(orden.getNumeroOrden());
        dto.setFechaOrden(orden.getFechaOrden());
        dto.setTotal(orden.getTotal());
        dto.setEstado(orden.getEstado().name());
        dto.setMetodoPago(orden.getMetodoPago());
        
        // Convertir detalles de la orden
        if (orden.getDetalles() != null && !orden.getDetalles().isEmpty()) {
            List<DetalleOrdenDTO> detallesDTO = orden.getDetalles().stream()
                .map(OrdenMapper::toDetalleDTO)
                .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
    
    /**
     * Convierte Entity Orden a OrdenResumenDTO
     */
    public static OrdenResumenDTO toResumenDTO(Orden orden) {
        if (orden == null) return null;
        
        OrdenResumenDTO dto = new OrdenResumenDTO();
        dto.setId(orden.getIdOrden());
        dto.setNumeroOrden(orden.getNumeroOrden());
        dto.setFechaOrden(orden.getFechaOrden());
        dto.setEstado(orden.getEstado().name());
        dto.setTotal(orden.getTotal());
        
        // Calcular cantidad de items
        if (orden.getDetalles() != null) {
            int cantidadItems = orden.getDetalles().stream()
                .mapToInt(DetalleOrden::getCantidad)
                .sum();
            dto.setCantidadItems(cantidadItems);
        } else {
            dto.setCantidadItems(0);
        }
        
        return dto;
    }
    
    /**
     * Convierte Entity DetalleOrden a DetalleOrdenDTO
     */
    public static DetalleOrdenDTO toDetalleDTO(DetalleOrden detalle) {
        if (detalle == null) return null;
        
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setImagenProducto(detalle.getProducto().getImagenUrl());
        dto.setTipoProducto(detalle.getProducto().getTipoProducto().name());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        
        return dto;
    }
}