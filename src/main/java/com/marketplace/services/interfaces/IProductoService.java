package com.marketplace.services.interfaces;

import com.marketplace.dto.producto.*;
import com.marketplace.enums.TipoProducto;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de Producto
 * @author Felipe Moreno
 */
public interface IProductoService {
    
    /**
     * Crear nuevo producto
     * @param dto Datos del producto
     * @param idVendedor ID del vendedor
     * @return Producto creado
     */
    ProductoResponseDTO crear(ProductoCreacionDTO dto, Long idVendedor);
    
    /**
     * Actualizar producto existente
     * @param idProducto ID del producto
     * @param dto Datos actualizados
     * @param idVendedor ID del vendedor (para validación)
     * @return Producto actualizado
     */
    ProductoResponseDTO actualizar(Long idProducto, ProductoCreacionDTO dto, Long idVendedor);
    
    /**
     * Obtener producto por ID
     * @param idProducto ID del producto
     * @return Datos del producto
     */
    ProductoResponseDTO obtenerPorId(Long idProducto);
    
    /**
     * Listar productos del vendedor
     * @param idVendedor ID del vendedor
     * @return Lista de productos
     */
    List<ProductoResponseDTO> listarMisProductos(Long idVendedor);
    
    /**
     * Cambiar estado del producto
     * @param idProducto ID del producto
     * @param idVendedor ID del vendedor (para validación)
     * @param estado Nuevo estado
     * @return true si se cambió exitosamente
     */
    boolean cambiarEstado(Long idProducto, Long idVendedor, boolean estado);
    
    /**
     * Listar productos del catálogo (activos y con stock)
     * @return Lista de productos disponibles
     */
    List<ProductoCatalogoDTO> listarCatalogo();
    
    /**
     * Buscar productos por término
     * @param termino Término de búsqueda
     * @return Lista de productos
     */
    List<ProductoCatalogoDTO> buscarProductos(String termino);
    
    /**
     * Listar productos por categoría
     * @param idCategoria ID de la categoría
     * @return Lista de productos
     */
    List<ProductoCatalogoDTO> listarPorCategoria(Long idCategoria);
    
    /**
     * Listar productos por tipo
     * @param tipo Tipo de producto
     * @return Lista de productos
     */
    List<ProductoCatalogoDTO> listarPorTipo(TipoProducto tipo);
    
    /**
     * Obtener detalle público del producto
     * @param idProducto ID del producto
     * @return Detalle del producto
     */
    ProductoCatalogoDTO obtenerDetalleCatalogo(Long idProducto);
    
    /**
     * Verificar si un producto pertenece a un vendedor
     * @param idProducto ID del producto
     * @param idVendedor ID del vendedor
     * @return true si pertenece
     */
    boolean perteneceAVendedor(Long idProducto, Long idVendedor);
}