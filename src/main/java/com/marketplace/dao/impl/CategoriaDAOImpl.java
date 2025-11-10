package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.ICategoriaDAO;
import com.marketplace.entities.Categoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para Categoría
 * @author Felipe Moreno
 */
@Stateless
public class CategoriaDAOImpl implements ICategoriaDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Categoria crear(Categoria categoria) {
        em.persist(categoria);
        em.flush();
        return categoria;
    }
    
    @Override
    public Categoria actualizar(Categoria categoria) {
        Categoria updated = em.merge(categoria);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        try {
            Categoria categoria = em.find(Categoria.class, id);
            return Optional.ofNullable(categoria);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        try {
            TypedQuery<Categoria> query = em.createQuery(
                "SELECT c FROM Categoria c WHERE c.nombre = :nombre", 
                Categoria.class
            );
            query.setParameter("nombre", nombre);
            Categoria categoria = query.getSingleResult();
            return Optional.of(categoria);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Categoria> listarTodas() {
        TypedQuery<Categoria> query = em.createQuery(
            "SELECT c FROM Categoria c ORDER BY c.nombre", 
            Categoria.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Categoria> listarActivas() {
        TypedQuery<Categoria> query = em.createQuery(
            "SELECT c FROM Categoria c WHERE c.estado = true ORDER BY c.nombre", 
            Categoria.class
        );
        return query.getResultList();
    }
    
    @Override
    public boolean cambiarEstado(Long id, boolean estado) {
        try {
            int updated = em.createQuery(
                "UPDATE Categoria c SET c.estado = :estado WHERE c.idCategoria = :id"
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
    public boolean existeNombre(String nombre) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Categoria c WHERE c.nombre = :nombre", 
            Long.class
        );
        query.setParameter("nombre", nombre);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public boolean existeNombreExcluyendo(String nombre, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Categoria c WHERE c.nombre = :nombre AND c.idCategoria != :id", 
            Long.class
        );
        query.setParameter("nombre", nombre);
        query.setParameter("id", idExcluir);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public Long contarTotal() {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Categoria c", 
            Long.class
        );
        return query.getSingleResult();
    }
    
    @Override
    public Long contarActivas() {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Categoria c WHERE c.estado = true", 
            Long.class
        );
        return query.getSingleResult();
    }
}