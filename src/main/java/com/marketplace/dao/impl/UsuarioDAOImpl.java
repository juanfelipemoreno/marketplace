package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IUsuarioDAO;
import com.marketplace.entities.Usuario;
import com.marketplace.enums.RolUsuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para Usuario
 * @author Felipe Moreno
 */
@Stateless
public class UsuarioDAOImpl implements IUsuarioDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Usuario crear(Usuario usuario) {
        em.persist(usuario);
        em.flush();
        return usuario;
    }
    
    @Override
    public Usuario actualizar(Usuario usuario) {
        Usuario updated = em.merge(usuario);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", 
                Usuario.class
            );
            query.setParameter("email", email);
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Usuario> listarTodos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u ORDER BY u.fechaRegistro DESC", 
            Usuario.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Usuario> listarPorRol(RolUsuario rol) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.rol = :rol ORDER BY u.nombre", 
            Usuario.class
        );
        query.setParameter("rol", rol);
        return query.getResultList();
    }
    
    @Override
    public List<Usuario> listarActivos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.estado = true ORDER BY u.nombre", 
            Usuario.class
        );
        return query.getResultList();
    }
    
    @Override
    public boolean existeEmail(String email) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", 
            Long.class
        );
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public boolean cambiarEstado(Long id, boolean estado) {
        try {
            int updated = em.createQuery(
                "UPDATE Usuario u SET u.estado = :estado WHERE u.idUsuario = :id"
            )
            .setParameter("estado", estado)
            .setParameter("id", id)
            .executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean cambiarRol(Long id, RolUsuario nuevoRol) {
        try {
            int updated = em.createQuery(
                "UPDATE Usuario u SET u.rol = :rol WHERE u.idUsuario = :id"
            )
            .setParameter("rol", nuevoRol)
            .setParameter("id", id)
            .executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Long contarTotal() {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM Usuario u", 
            Long.class
        );
        return query.getSingleResult();
    }
    
    @Override
    public Long contarPorRol(RolUsuario rol) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol", 
            Long.class
        );
        query.setParameter("rol", rol);
        return query.getSingleResult();
    }
}