package com.marketplace.dao.interfaces;

import com.marketplace.entities.InventarioDigital;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de InventarioDigital
 * @author Felipe Moreno
 */
public interface IInventarioDigitalDAO {
    
    /**
     * Crear nuevo inventario digital
     * @param inventario Inventario a crear
     * @return Inventario creado con ID generado
     */
    InventarioDigital crear(InventarioDigital inventario);
    
    /**
     * Actualizar inventario digital
     * @param inventario Inventario con datos actualizados
     * @return Inventario actualizado
     */
    InventarioDigital actualizar(InventarioDigital inventario);
    
    /**
     * Buscar inventario por ID
     * @param id ID del inventario
     * @return Optional con el inventario si existe
     */
    Optional<InventarioDigital> buscarPorId(Long id);
    
    /**
     * Buscar inventario por producto
     * @param idProducto ID del producto
     * @return Optional con el inventario si existe
     */
    Optional<InventarioDigital> buscarPorProducto(Long idProducto);
    
    /**
     * Reducir licencias disponibles (al realizar venta)
     * @param idProducto ID del producto
     * @param cantidad Cantidad de licencias a reducir
     * @return true si se redujo exitosamente
     */
    boolean reducirLicencias(Long idProducto, Integer cantidad);
    
    /**
     * Aumentar licencias disponibles (al agregar más)
     * @param idProducto ID del producto
     * @param cantidad Cantidad de licencias a aumentar
     * @return true si se aumentó exitosamente
     */
    boolean aumentarLicencias(Long idProducto, Integer cantidad);
    
    /**
     * Verificar si hay licencias disponibles
     * @param idProducto ID del producto
     * @param cantidadRequerida Cantidad de licencias requeridas
     * @return true si hay licencias suficientes
     */
    boolean verificarDisponibilidad(Long idProducto, Integer cantidadRequerida);
    
    /**
     * Listar productos digitales con licencias bajas (menos de 5)
     * @return Lista de inventarios con licencias bajas
     */
    List<InventarioDigital> listarConLicenciasBajas();
    
    /**
     * Listar productos digitales sin licencias
     * @return Lista de inventarios sin licencias
     */
    List<InventarioDigital> listarSinLicencias();
    
    /**
     * Obtener licencias disponibles de un producto
     * @param idProducto ID del producto
     * @return Cantidad de licencias disponibles
     */
    Integer obtenerLicenciasDisponibles(Long idProducto);
    
    /**
     * Obtener URL del archivo del producto digital
     * @param idProducto ID del producto
     * @return URL del archivo
     */
    String obtenerArchivoUrl(Long idProducto);
}