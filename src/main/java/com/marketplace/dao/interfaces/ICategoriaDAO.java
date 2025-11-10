package com.marketplace.dao.interfaces;

import com.marketplace.entities.Categoria;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones de Categoría
 * @author Felipe Moreno
 */
public interface ICategoriaDAO {
    
    /**
     * Crear nueva categoría
     * @param categoria Categoría a crear
     * @return Categoría creada con ID generado
     */
    Categoria crear(Categoria categoria);
    
    /**
     * Actualizar categoría existente
     * @param categoria Categoría con datos actualizados
     * @return Categoría actualizada
     */
    Categoria actualizar(Categoria categoria);
    
    /**
     * Buscar categoría por ID
     * @param id ID de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Categoria> buscarPorId(Long id);
    
    /**
     * Buscar categoría por nombre
     * @param nombre Nombre de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Categoria> buscarPorNombre(String nombre);
    
    /**
     * Listar todas las categorías
     * @return Lista de todas las categorías
     */
    List<Categoria> listarTodas();
    
    /**
     * Listar categorías activas
     * @return Lista de categorías activas
     */
    List<Categoria> listarActivas();
    
    /**
     * Cambiar estado de la categoría
     * @param id ID de la categoría
     * @param estado Nuevo estado
     * @return true si se cambió exitosamente
     */
    boolean cambiarEstado(Long id, boolean estado);
    
    /**
     * Verificar si existe una categoría con ese nombre
     * @param nombre Nombre a verificar
     * @return true si existe
     */
    boolean existeNombre(String nombre);
    
    /**
     * Verificar si existe una categoría con ese nombre excluyendo un ID
     * @param nombre Nombre a verificar
     * @param idExcluir ID a excluir de la búsqueda
     * @return true si existe
     */
    boolean existeNombreExcluyendo(String nombre, Long idExcluir);
    
    /**
     * Contar total de categorías
     * @return Cantidad total de categorías
     */
    Long contarTotal();
    
    /**
     * Contar categorías activas
     * @return Cantidad de categorías activas
     */
    Long contarActivas();
}