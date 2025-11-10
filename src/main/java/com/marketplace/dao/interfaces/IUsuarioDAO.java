package com.marketplace.dao.interfaces;

import com.marketplace.entities.Usuario;
import com.marketplace.enums.RolUsuario;
import java.util.List;
import java.util.Optional;

/**
 * @author Felipe Moreno
 */
public interface IUsuarioDAO {
    
    /**
     * Crear nuevo usuario
     * @param usuario Usuario a crear
     * @return Usuario creado con ID generado
     */
    Usuario crear(Usuario usuario);
    
    /**
     * Actualizar usuario existente
     * @param usuario Usuario con datos actualizados
     * @return Usuario actualizado
     */
    Usuario actualizar(Usuario usuario);
    
    /**
     * Buscar usuario por ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorId(Long id);
    
    /**
     * Buscar usuario por email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorEmail(String email);
    
    /**
     * Listar todos los usuarios
     * @return Lista de todos los usuarios
     */
    List<Usuario> listarTodos();
    
    /**
     * Listar usuarios por rol
     * @param rol Rol a filtrar
     * @return Lista de usuarios con ese rol
     */
    List<Usuario> listarPorRol(RolUsuario rol);
    
    /**
     * Listar usuarios activos
     * @return Lista de usuarios activos
     */
    List<Usuario> listarActivos();
    
    /**
     * Verificar si existe un email
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    boolean existeEmail(String email);
    
    /**
     * Cambiar estado del usuario (activar/desactivar)
     * @param id ID del usuario
     * @param estado Nuevo estado
     * @return true si se cambió exitosamente
     */
    boolean cambiarEstado(Long id, boolean estado);
    
    /**
     * Cambiar rol del usuario
     * @param id ID del usuario
     * @param nuevoRol Nuevo rol
     * @return true si se cambió exitosamente
     */
    boolean cambiarRol(Long id, RolUsuario nuevoRol);
    
    /**
     * Contar total de usuarios
     * @return Cantidad total de usuarios
     */
    Long contarTotal();
    
    /**
     * Contar usuarios por rol
     * @param rol Rol a contar
     * @return Cantidad de usuarios con ese rol
     */
    Long contarPorRol(RolUsuario rol);
}