package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.IDescargaDAO;
import com.marketplace.dto.orden.DescargaResponseDTO;
import com.marketplace.entities.Descarga;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.DescargaMapper;
import com.marketplace.services.interfaces.IDescargaService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Descarga
 * @author Felipe Moreno
 */
@Stateless
public class DescargaServiceImpl implements IDescargaService {
    
    @EJB
    private IDescargaDAO descargaDAO;
    
    @Override
    public List<DescargaResponseDTO> listarMisDescargas(Long idUsuario) {
        return descargaDAO.listarPorUsuario(idUsuario).stream()
                .map(DescargaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DescargaResponseDTO> listarDescargasDisponibles(Long idUsuario) {
        return descargaDAO.obtenerDescargasDisponibles(idUsuario).stream()
                .map(DescargaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DescargaResponseDTO obtenerInformacionDescarga(Long idUsuario, Long idProducto) {
        Descarga descarga = descargaDAO.buscarPorUsuarioYProducto(idUsuario, idProducto)
                .orElseThrow(() -> new NotFoundException(
                    "No se encontró registro de descarga para este producto"
                ));
        
        return DescargaMapper.toResponseDTO(descarga);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public DescargaResponseDTO registrarDescarga(Long idUsuario, Long idProducto) {
        // Buscar descarga
        Descarga descarga = descargaDAO.buscarPorUsuarioYProducto(idUsuario, idProducto)
                .orElseThrow(() -> new BusinessException(
                    "No tiene permiso para descargar este producto"
                ));
        
        // Verificar límite de descargas
        if (descarga.getNumeroDescargas() >= descarga.getLimiteDescargas()) {
            throw new BusinessException(
                "Ha alcanzado el límite de descargas para este producto (" + 
                descarga.getLimiteDescargas() + " descargas)"
            );
        }
        
        // Incrementar contador
        boolean incrementado = descargaDAO.incrementarContador(descarga.getIdDescarga());
        
        if (!incrementado) {
            throw new BusinessException("Error al registrar la descarga");
        }
        
        // Obtener descarga actualizada
        Descarga descargaActualizada = descargaDAO.buscarPorId(descarga.getIdDescarga()).get();
        return DescargaMapper.toResponseDTO(descargaActualizada);
    }
    
    @Override
    public boolean puedeDescargar(Long idUsuario, Long idProducto) {
        return descargaDAO.puedeDescargar(idUsuario, idProducto);
    }
    
    @Override
    public boolean haComprado(Long idUsuario, Long idProducto) {
        return descargaDAO.haComprado(idUsuario, idProducto);
    }
    
    @Override
    public String obtenerUrlDescarga(Long idUsuario, Long idProducto) {
        Descarga descarga = descargaDAO.buscarPorUsuarioYProducto(idUsuario, idProducto)
                .orElseThrow(() -> new BusinessException(
                    "No tiene permiso para descargar este producto"
                ));
        
        // Verificar límite
        if (descarga.getNumeroDescargas() >= descarga.getLimiteDescargas()) {
            throw new BusinessException(
                "Ha alcanzado el límite de descargas para este producto"
            );
        }
        
        // Obtener URL del inventario digital
        if (descarga.getProducto().getInventarioDigital() != null) {
            return descarga.getProducto().getInventarioDigital().getArchivoUrl();
        }
        
        throw new BusinessException("No se encontró el archivo del producto");
    }
}