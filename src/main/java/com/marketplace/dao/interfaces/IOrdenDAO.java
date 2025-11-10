package com.marketplace.dao.interfaces;

import com.marketplace.entities.Orden;
import com.marketplace.enums.EstadoOrden;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de Orden
 * @author Felipe Moreno
 */
public interface IOrdenDAO {
    
    /**
     * Crear nueva orden
     * @param orden Orden a crear
     * @return Orden creada con ID generado
     */
    Orden crear(Orden orden);
    
    /**
     * Actualizar orden existente
     * @param orden Orden con datos actualizados
     * @return Orden actualizada
     */
    Orden actualizar(Orden orden);
    
    /**
     * Buscar orden por ID
     * @param id ID de la orden
     * @return Optional con la orden si existe
     */
    Optional<Orden> buscarPorId(Long id);
    
    /**
     * Buscar orden por número de orden
     * @param numeroOrden Número de orden
     * @return Optional con la orden si existe
     */
    Optional<Orden> buscarPorNumeroOrden(String numeroOrden);
    
    /**
     * Listar todas las órdenes
     * @return Lista de todas las órdenes
     */
    List<Orden> listarTodas();
    
    /**
     * Listar órdenes por usuario
     * @param idUsuario ID del usuario
     * @return Lista de órdenes del usuario
     */
    List<Orden> listarPorUsuario(Long idUsuario);
    
    /**
     * Listar órdenes por estado
     * @param estado Estado de la orden
     * @return Lista de órdenes con ese estado
     */
    List<Orden> listarPorEstado(EstadoOrden estado);
    
    /**
     * Listar órdenes por rango de fechas
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Lista de órdenes en el rango
     */
    List<Orden> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Listar ventas de un vendedor
     * Órdenes que contienen productos del vendedor
     * @param idVendedor ID del vendedor
     * @return Lista de órdenes con productos del vendedor
     */
    List<Orden> listarVentasPorVendedor(Long idVendedor);
    
    /**
     * Cambiar estado de la orden
     * @param id ID de la orden
     * @param nuevoEstado Nuevo estado
     * @return true si se cambió exitosamente
     */
    boolean cambiarEstado(Long id, EstadoOrden nuevoEstado);
    
    /**
     * Verificar si una orden pertenece a un usuario
     * @param idOrden ID de la orden
     * @param idUsuario ID del usuario
     * @return true si la orden pertenece al usuario
     */
    boolean perteneceAUsuario(Long idOrden, Long idUsuario);
    
    /**
     * Contar órdenes por usuario
     * @param idUsuario ID del usuario
     * @return Cantidad de órdenes del usuario
     */
    Long contarPorUsuario(Long idUsuario);
    
    /**
     * Contar órdenes por estado
     * @param estado Estado a contar
     * @return Cantidad de órdenes en ese estado
     */
    Long contarPorEstado(EstadoOrden estado);
    
    /**
     * Obtener total de ventas por vendedor en un período
     * @param idVendedor ID del vendedor
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Total de ventas
     */
    Double obtenerTotalVentasPorVendedor(Long idVendedor, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}