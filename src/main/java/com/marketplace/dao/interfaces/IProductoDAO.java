package com.marketplace.dao.interfaces;

import com.marketplace.entities.Producto;
import com.marketplace.enums.TipoProducto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de Producto
 * @author Felipe Moreno
 */
public interface IProductoDAO {
    
    /**
     * Crear nuevo producto
     * @param producto Producto a crear
     * @return Producto creado con ID generado
     */
    Producto crear(Producto producto);
    
    /**
     * Actualizar producto existente
     * @param producto Producto con datos actualizados
     * @return Producto actualizado
     */
    Producto actualizar(Producto producto);
    
    /**
     * Buscar producto por ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    Optional<Producto> buscarPorId(Long id);
    
    /**
     * Listar todos los productos
     * @return Lista de todos los productos
     */
    List<Producto> listarTodos();
    
    /**
     * Listar productos activos
     * @return Lista de productos activos
     */
    List<Producto> listarActivos();
    
    /**
     * Listar productos por vendedor
     * @param idVendedor ID del vendedor
     * @return Lista de productos del vendedor
     */
    List<Producto> listarPorVendedor(Long idVendedor);
    
    /**
     * Listar productos por categoría
     * @param idCategoria ID de la categoría
     * @return Lista de productos de la categoría
     */
    List<Producto> listarPorCategoria(Long idCategoria);
    
    /**
     * Listar productos por tipo
     * @param tipo Tipo de producto (FISICO o DIGITAL)
     * @return Lista de productos del tipo especificado
     */
    List<Producto> listarPorTipo(TipoProducto tipo);
    
    /**
     * Buscar productos por nombre o descripción
     * @param termino Término de búsqueda
     * @return Lista de productos que coinciden
     */
    List<Producto> buscarPorTermino(String termino);
    
    /**
     * Listar productos activos con stock disponible
     * @return Lista de productos disponibles para venta
     */
    List<Producto> listarDisponibles();
    
    /**
     * Listar productos por categoría activos
     * @param idCategoria ID de la categoría
     * @return Lista de productos activos de la categoría
     */
    List<Producto> listarPorCategoriaActivos(Long idCategoria);
    
    /**
     * Cambiar estado del producto
     * @param id ID del producto
     * @param estado Nuevo estado
     * @return true si se cambió exitosamente
     */
    boolean cambiarEstado(Long id, boolean estado);
    
    /**
     * Verificar si un producto pertenece a un vendedor
     * @param idProducto ID del producto
     * @param idVendedor ID del vendedor
     * @return true si el producto pertenece al vendedor
     */
    boolean perteneceAVendedor(Long idProducto, Long idVendedor);
    
    /**
     * Listar productos con stock bajo (menos de 5 unidades)
     * @return Lista de productos con stock bajo
     */
    List<Producto> listarConStockBajo();
    
    /**
     * Contar productos por vendedor
     * @param idVendedor ID del vendedor
     * @return Cantidad de productos del vendedor
     */
    Long contarPorVendedor(Long idVendedor);
    
    /**
     * Contar productos por categoría
     * @param idCategoria ID de la categoría
     * @return Cantidad de productos en la categoría
     */
    Long contarPorCategoria(Long idCategoria);
}