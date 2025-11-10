package com.marketplace.mappers;

import com.marketplace.dto.carrito.*;
import com.marketplace.entities.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CarritoMapper {
    
    /**
     * Convierte Entity Carrito a CarritoResponseDTO
     */
    public static CarritoResponseDTO toResponseDTO(Carrito carrito) {
        if (carrito == null) return null;
        
        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setIdCarrito(carrito.getIdCarrito());
        
        // Convertir items del carrito
        if (carrito.getItems() != null && !carrito.getItems().isEmpty()) {
            List<ItemCarritoResponseDTO> itemsDTO = carrito.getItems().stream()
                .map(CarritoMapper::toItemResponseDTO)
                .collect(Collectors.toList());
            dto.setItems(itemsDTO);
            
            // Calcular totales
            int totalItems = carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
            dto.setTotalItems(totalItems);
            
            BigDecimal totalPrecio = carrito.getItems().stream()
                .map(item -> item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalPrecio(totalPrecio);
        } else {
            dto.setTotalItems(0);
            dto.setTotalPrecio(BigDecimal.ZERO);
        }
        
        return dto;
    }
    
    /**
     * Convierte Entity ItemCarrito a ItemCarritoResponseDTO
     */
    public static ItemCarritoResponseDTO toItemResponseDTO(ItemCarrito item) {
        if (item == null) return null;
        
        ItemCarritoResponseDTO dto = new ItemCarritoResponseDTO();
        dto.setIdItem(item.getIdItem());
        dto.setIdProducto(item.getProducto().getIdProducto());
        dto.setNombreProducto(item.getProducto().getNombre());
        dto.setImagenProducto(item.getProducto().getImagenUrl());
        dto.setTipoProducto(item.getProducto().getTipoProducto().name());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        
        // Calcular subtotal
        BigDecimal subtotal = item.getPrecioUnitario()
            .multiply(BigDecimal.valueOf(item.getCantidad()));
        dto.setSubtotal(subtotal);
        
        // Información de disponibilidad
        if (item.getProducto().getInventarioFisico() != null) {
            dto.setDisponibilidad(item.getProducto()
                .getInventarioFisico().getCantidadDisponible());
        } else if (item.getProducto().getInventarioDigital() != null) {
            dto.setDisponibilidad(item.getProducto()
                .getInventarioDigital().getLicenciasDisponibles());
        }
        
        return dto;
    }
}