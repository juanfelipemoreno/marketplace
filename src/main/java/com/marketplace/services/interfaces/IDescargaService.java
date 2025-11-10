package com.marketplace.services.interfaces;

import com.marketplace.dto.orden.DescargaResponseDTO;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de Descarga
 * @author Felipe Moreno
 */
public interface IDescargaService {
    
    /**
     * Listar todas las descargas de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de descargas
     */
    List<DescargaResponseDTO> listarMisDescargas(Long idUsuario);
    
    /**
     * Listar descargas disponibles (que no han alcanzado el límite)
     * @param idUsuario ID del usuario
     * @return Lista de descargas disponibles
     */
    List<DescargaResponseDTO> listarDescargasDisponibles(Long idUsuario);
    
    /**
     * Obtener información de descarga de un producto
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return Información de la descarga
     */
    DescargaResponseDTO obtenerInformacionDescarga(Long idUsuario, Long idProducto);
    
    /**
     * Registrar una descarga (incrementar contador)
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return Descarga actualizada
     */
    DescargaResponseDTO registrarDescarga(Long idUsuario, Long idProducto);
    
    /**
     * Verificar si un usuario puede descargar un producto
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return true si puede descargar
     */
    boolean puedeDescargar(Long idUsuario, Long idProducto);
    
    /**
     * Verificar si un usuario ha comprado un producto digital
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return true si ha comprado
     */
    boolean haComprado(Long idUsuario, Long idProducto);
    
    /**
     * Obtener URL de descarga del archivo
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return URL del archivo
     */
    String obtenerUrlDescarga(Long idUsuario, Long idProducto);
}