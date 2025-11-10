package com.marketplace.services.interfaces;

import com.marketplace.dto.categoria.*;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de Categoría
 * @author Felipe Moreno
 */
public interface ICategoriaService {
    
    /**
     * Crear nueva categoría (solo admin)
     * @param dto Datos de la categoría
     * @return Categoría creada
     */
    CategoriaResponseDTO crear(CategoriaDTO dto);
    
    /**
     * Actualizar categoría existente (solo admin)
     * @param idCategoria ID de la categoría
     * @param dto Datos actualizados
     * @return Categoría actualizada
     */
    CategoriaResponseDTO actualizar(Long idCategoria, CategoriaDTO dto);
    
    /**
     * Obtener categoría por ID
     * @param idCategoria ID de la categoría
     * @return Datos de la categoría
     */
    CategoriaResponseDTO obtenerPorId(Long idCategoria);
    
    /**
     * Listar todas las categorías
     * @return Lista de categorías
     */
    List<CategoriaResponseDTO> listarTodas();
    
    /**
     * Listar categorías activas
     * @return Lista de categorías activas
     */
    List<CategoriaResponseDTO> listarActivas();
    
    /**
     * Cambiar estado de la categoría (solo admin)
     * @param idCategoria ID de la categoría
     * @param estado Nuevo estado
     * @return Categoría actualizada
     */
    CategoriaResponseDTO cambiarEstado(Long idCategoria, boolean estado);
    
    /**
     * Verificar si existe una categoría con ese nombre
     * @param nombre Nombre a verificar
     * @return true si existe
     */
    boolean existeNombre(String nombre);
}