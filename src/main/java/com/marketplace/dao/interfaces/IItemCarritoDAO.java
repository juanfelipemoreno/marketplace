package com.marketplace.dao.interfaces;

import com.marketplace.entities.ItemCarrito;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de ItemCarrito
 * @author Felipe Moreno
 */
public interface IItemCarritoDAO {
    
    /**
     * Crear nuevo item en el carrito
     * @param item Item a crear
     * @return Item creado con ID generado
     */
    ItemCarrito crear(ItemCarrito item);
    
    /**
     * Actualizar item del carrito
     * @param item Item con datos actualizados
     * @return Item actualizado
     */
    ItemCarrito actualizar(ItemCarrito item);
    
    /**
     * Buscar item por ID
     * @param id ID del item
     * @return Optional con el item si existe
     */
    Optional<ItemCarrito> buscarPorId(Long id);
    
    /**
     * Buscar item por carrito y producto
     * @param idCarrito ID del carrito
     * @param idProducto ID del producto
     * @return Optional con el item si existe
     */
    Optional<ItemCarrito> buscarPorCarritoYProducto(Long idCarrito, Long idProducto);
    
    /**
     * Listar items de un carrito
     * @param idCarrito ID del carrito
     * @return Lista de items del carrito
     */
    List<ItemCarrito> listarPorCarrito(Long idCarrito);
    
    /**
     * Eliminar item del carrito
     * @param id ID del item
     * @return true si se eliminó exitosamente
     */
    boolean eliminar(Long id);
    
    /**
     * Eliminar todos los items de un carrito
     * @param idCarrito ID del carrito
     * @return Cantidad de items eliminados
     */
    int eliminarPorCarrito(Long idCarrito);
    
    /**
     * Contar items de un carrito
     * @param idCarrito ID del carrito
     * @return Cantidad de items en el carrito
     */
    Long contarPorCarrito(Long idCarrito);
    
    /**
     * Verificar si existe un producto en el carrito
     * @param idCarrito ID del carrito
     * @param idProducto ID del producto
     * @return true si el producto está en el carrito
     */
    boolean existeProductoEnCarrito(Long idCarrito, Long idProducto);
}