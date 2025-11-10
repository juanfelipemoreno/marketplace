package com.marketplace.dao.interfaces;

import com.marketplace.entities.DetalleOrden;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de DetalleOrden
 * @author Felipe Moreno
 */
public interface IDetalleOrdenDAO {
    
    /**
     * Crear nuevo detalle de orden
     * @param detalle Detalle a crear
     * @return Detalle creado con ID generado
     */
    DetalleOrden crear(DetalleOrden detalle);
    
    /**
     * Buscar detalle por ID
     * @param id ID del detalle
     * @return Optional con el detalle si existe
     */
    Optional<DetalleOrden> buscarPorId(Long id);
    
    /**
     * Listar detalles de una orden
     * @param idOrden ID de la orden
     * @return Lista de detalles de la orden
     */
    List<DetalleOrden> listarPorOrden(Long idOrden);
    
    /**
     * Listar detalles que contienen un producto específico
     * @param idProducto ID del producto
     * @return Lista de detalles con ese producto
     */
    List<DetalleOrden> listarPorProducto(Long idProducto);
    
    /**
     * Contar cantidad de veces que se ha vendido un producto
     * @param idProducto ID del producto
     * @return Cantidad total vendida
     */
    Integer contarCantidadVendida(Long idProducto);
    
    /**
     * Obtener productos más vendidos
     * @param limite Cantidad máxima de resultados
     * @return Lista de IDs de productos más vendidos
     */
    List<Object[]> obtenerProductosMasVendidos(int limite);
}