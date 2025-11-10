package com.marketplace.services.interfaces;

import com.marketplace.dto.carrito.*;

/**
 * Interfaz de servicio para operaciones de Carrito
 * @author Felipe Moreno
 */
public interface ICarritoService {
    
    /**
     * Obtener carrito del usuario
     * @param idUsuario ID del usuario
     * @return Carrito con sus items
     */
    CarritoResponseDTO obtenerCarrito(Long idUsuario);
    
    /**
     * Agregar producto al carrito
     * @param idUsuario ID del usuario
     * @param dto Datos del item a agregar
     * @return Carrito actualizado
     */
    CarritoResponseDTO agregarItem(Long idUsuario, ItemCarritoDTO dto);
    
    /**
     * Actualizar cantidad de un item
     * @param idUsuario ID del usuario
     * @param idItem ID del item
     * @param nuevaCantidad Nueva cantidad
     * @return Carrito actualizado
     */
    CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idItem, Integer nuevaCantidad);
    
    /**
     * Eliminar item del carrito
     * @param idUsuario ID del usuario
     * @param idItem ID del item
     * @return Carrito actualizado
     */
    CarritoResponseDTO eliminarItem(Long idUsuario, Long idItem);
    
    /**
     * Vaciar carrito
     * @param idUsuario ID del usuario
     * @return true si se vació exitosamente
     */
    boolean vaciarCarrito(Long idUsuario);
    
    /**
     * Validar disponibilidad de todos los items del carrito
     * @param idUsuario ID del usuario
     * @return true si todos los items están disponibles
     */
    boolean validarDisponibilidad(Long idUsuario);
}