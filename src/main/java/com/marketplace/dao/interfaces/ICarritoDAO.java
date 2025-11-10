package com.marketplace.dao.interfaces;

import com.marketplace.entities.Carrito;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de Carrito
 * @author Felipe Moreno
 */
public interface ICarritoDAO {
    
    /**
     * Crear nuevo carrito
     * @param carrito Carrito a crear
     * @return Carrito creado con ID generado
     */
    Carrito crear(Carrito carrito);
    
    /**
     * Actualizar carrito existente
     * @param carrito Carrito con datos actualizados
     * @return Carrito actualizado
     */
    Carrito actualizar(Carrito carrito);
    
    /**
     * Buscar carrito por ID
     * @param id ID del carrito
     * @return Optional con el carrito si existe
     */
    Optional<Carrito> buscarPorId(Long id);
    
    /**
     * Buscar carrito por usuario
     * @param idUsuario ID del usuario
     * @return Optional con el carrito del usuario si existe
     */
    Optional<Carrito> buscarPorUsuario(Long idUsuario);
    
    /**
     * Buscar o crear carrito para un usuario
     * Si el usuario no tiene carrito, lo crea
     * @param idUsuario ID del usuario
     * @return Carrito del usuario
     */
    Carrito obtenerOCrearCarrito(Long idUsuario);
    
    /**
     * Eliminar carrito
     * @param id ID del carrito
     * @return true si se eliminó exitosamente
     */
    boolean eliminar(Long id);
    
    /**
     * Limpiar todos los items del carrito
     * @param idCarrito ID del carrito
     * @return true si se limpiaron los items
     */
    boolean limpiarItems(Long idCarrito);
    
    /**
     * Verificar si un carrito pertenece a un usuario
     * @param idCarrito ID del carrito
     * @param idUsuario ID del usuario
     * @return true si el carrito pertenece al usuario
     */
    boolean perteneceAUsuario(Long idCarrito, Long idUsuario);
}