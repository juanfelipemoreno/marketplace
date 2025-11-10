package com.marketplace.dao.interfaces;

import com.marketplace.entities.InventarioFisico;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de InventarioFisico
 * @author Felipe Moreno
 */
public interface IInventarioFisicoDAO {
    
    /**
     * Crear nuevo inventario físico
     * @param inventario Inventario a crear
     * @return Inventario creado con ID generado
     */
    InventarioFisico crear(InventarioFisico inventario);
    
    /**
     * Actualizar inventario físico
     * @param inventario Inventario con datos actualizados
     * @return Inventario actualizado
     */
    InventarioFisico actualizar(InventarioFisico inventario);
    
    /**
     * Buscar inventario por ID
     * @param id ID del inventario
     * @return Optional con el inventario si existe
     */
    Optional<InventarioFisico> buscarPorId(Long id);
    
    /**
     * Buscar inventario por producto
     * @param idProducto ID del producto
     * @return Optional con el inventario si existe
     */
    Optional<InventarioFisico> buscarPorProducto(Long idProducto);
    
    /**
     * Reducir cantidad disponible (al realizar venta)
     * @param idProducto ID del producto
     * @param cantidad Cantidad a reducir
     * @return true si se redujo exitosamente
     */
    boolean reducirCantidad(Long idProducto, Integer cantidad);
    
    /**
     * Aumentar cantidad disponible (al reponer stock)
     * @param idProducto ID del producto
     * @param cantidad Cantidad a aumentar
     * @return true si se aumentó exitosamente
     */
    boolean aumentarCantidad(Long idProducto, Integer cantidad);
    
    /**
     * Verificar si hay stock disponible
     * @param idProducto ID del producto
     * @param cantidadRequerida Cantidad requerida
     * @return true si hay stock suficiente
     */
    boolean verificarDisponibilidad(Long idProducto, Integer cantidadRequerida);
    
    /**
     * Listar productos con stock bajo (menos de 5 unidades)
     * @return Lista de inventarios con stock bajo
     */
    List<InventarioFisico> listarConStockBajo();
    
    /**
     * Listar productos sin stock
     * @return Lista de inventarios sin stock
     */
    List<InventarioFisico> listarSinStock();
    
    /**
     * Obtener cantidad disponible de un producto
     * @param idProducto ID del producto
     * @return Cantidad disponible
     */
    Integer obtenerCantidadDisponible(Long idProducto);
}