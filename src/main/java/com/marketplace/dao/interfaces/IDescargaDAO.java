package com.marketplace.dao.interfaces;

import com.marketplace.entities.Descarga;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de Descarga
 * @author Felipe Moreno
 */
public interface IDescargaDAO {
    
    /**
     * Crear nuevo registro de descarga
     * @param descarga Descarga a crear
     * @return Descarga creada con ID generado
     */
    Descarga crear(Descarga descarga);
    
    /**
     * Actualizar registro de descarga
     * @param descarga Descarga con datos actualizados
     * @return Descarga actualizada
     */
    Descarga actualizar(Descarga descarga);
    
    /**
     * Buscar descarga por ID
     * @param id ID de la descarga
     * @return Optional con la descarga si existe
     */
    Optional<Descarga> buscarPorId(Long id);
    
    /**
     * Buscar descarga por usuario y producto
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return Optional con la descarga si existe
     */
    Optional<Descarga> buscarPorUsuarioYProducto(Long idUsuario, Long idProducto);
    
    /**
     * Listar descargas de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de descargas del usuario
     */
    List<Descarga> listarPorUsuario(Long idUsuario);
    
    /**
     * Listar descargas de un producto
     * @param idProducto ID del producto
     * @return Lista de descargas del producto
     */
    List<Descarga> listarPorProducto(Long idProducto);
    
    /**
     * Incrementar contador de descargas
     * @param id ID de la descarga
     * @return true si se incrementó exitosamente
     */
    boolean incrementarContador(Long id);
    
    /**
     * Verificar si un usuario puede descargar un producto
     * (no ha excedido el límite)
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return true si puede descargar
     */
    boolean puedeDescargar(Long idUsuario, Long idProducto);
    
    /**
     * Verificar si un usuario ha comprado un producto
     * @param idUsuario ID del usuario
     * @param idProducto ID del producto
     * @return true si ha comprado el producto
     */
    boolean haComprado(Long idUsuario, Long idProducto);
    
    /**
     * Obtener productos digitales disponibles para descarga de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de descargas disponibles
     */
    List<Descarga> obtenerDescargasDisponibles(Long idUsuario);
    
    /**
     * Contar descargas totales de un producto
     * @param idProducto ID del producto
     * @return Total de descargas del producto
     */
    Long contarDescargasPorProducto(Long idProducto);
}