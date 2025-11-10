package com.marketplace.mappers;

import com.marketplace.dto.orden.DescargaResponseDTO;
import com.marketplace.entities.Descarga;

public class DescargaMapper {
    
    public static DescargaResponseDTO toResponseDTO(Descarga descarga) {
        if (descarga == null) return null;
        
        DescargaResponseDTO dto = new DescargaResponseDTO();
        dto.setIdProducto(descarga.getProducto().getIdProducto());
        dto.setNombreProducto(descarga.getProducto().getNombre());
        dto.setImagenProducto(descarga.getProducto().getImagenUrl());
        dto.setIdOrden(descarga.getOrden().getIdOrden());
        dto.setNumeroOrden(descarga.getOrden().getNumeroOrden());
        dto.setFechaCompra(descarga.getOrden().getFechaOrden());
        dto.setNumeroDescargas(descarga.getNumeroDescargas());
        dto.setLimiteDescargas(descarga.getLimiteDescargas());
        
        // Verificar si puede descargar más veces
        boolean puedeDescargar = descarga.getNumeroDescargas() < descarga.getLimiteDescargas();
        dto.setPuedeDescargar(puedeDescargar);
        
        // URL del archivo (desde inventario digital)
        if (descarga.getProducto().getInventarioDigital() != null) {
            dto.setArchivoUrl(descarga.getProducto().getInventarioDigital().getArchivoUrl());
            dto.setTamanoArchivo(descarga.getProducto().getInventarioDigital().getTamanoArchivo());
        }
        
        return dto;
    }
}