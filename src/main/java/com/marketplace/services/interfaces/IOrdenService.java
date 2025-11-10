package com.marketplace.services.interfaces;

import com.marketplace.dto.orden.*;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de Orden
 * @author Felipe Moreno
 */
public interface IOrdenService {
    
    /**
     * Procesar compra (checkout)
     * Convierte el carrito en una orden
     * @param idUsuario ID del usuario
     * @param dto Datos de la orden
     * @return Orden creada
     */
    OrdenResponseDTO procesarCompra(Long idUsuario, OrdenCreacionDTO dto);
    
    /**
     * Obtener orden por ID
     * @param idOrden ID de la orden
     * @param idUsuario ID del usuario (para validación)
     * @return Datos de la orden
     */
    OrdenResponseDTO obtenerOrden(Long idOrden, Long idUsuario);
    
    /**
     * Obtener orden por número de orden
     * @param numeroOrden Número de orden
     * @param idUsuario ID del usuario (para validación)
     * @return Datos de la orden
     */
    OrdenResponseDTO obtenerPorNumeroOrden(String numeroOrden, Long idUsuario);
    
    /**
     * Listar órdenes del usuario
     * @param idUsuario ID del usuario
     * @return Lista de órdenes
     */
    List<OrdenResumenDTO> listarMisOrdenes(Long idUsuario);
    
    /**
     * Listar ventas del vendedor
     * @param idVendedor ID del vendedor
     * @return Lista de ventas
     */
    List<OrdenResumenDTO> listarMisVentas(Long idVendedor);
    
    /**
     * Listar todas las órdenes (solo admin)
     * @return Lista de todas las órdenes
     */
    List<OrdenResumenDTO> listarTodas();
    
    /**
     * Cancelar orden
     * @param idOrden ID de la orden
     * @param idUsuario ID del usuario (para validación)
     * @return true si se canceló exitosamente
     */
    boolean cancelarOrden(Long idOrden, Long idUsuario);
}