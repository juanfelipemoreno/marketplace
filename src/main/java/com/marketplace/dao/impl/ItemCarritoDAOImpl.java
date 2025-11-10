package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IItemCarritoDAO;
import com.marketplace.entities.ItemCarrito;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para ItemCarrito
 * @author Felipe Moreno
 */
@Stateless
public class ItemCarritoDAOImpl implements IItemCarritoDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public ItemCarrito crear(ItemCarrito item) {
        em.persist(item);
        em.flush();
        return item;
    }
    
    @Override
    public ItemCarrito actualizar(ItemCarrito item) {
        ItemCarrito updated = em.merge(item);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<ItemCarrito> buscarPorId(Long id) {
        try {
            ItemCarrito item = em.find(ItemCarrito.class, id);
            return Optional.ofNullable(item);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<ItemCarrito> buscarPorCarritoYProducto(Long idCarrito, Long idProducto) {
        try {
            TypedQuery<ItemCarrito> query = em.createQuery(
                "SELECT i FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito " +
                "AND i.producto.idProducto = :idProducto", 
                ItemCarrito.class
            );
            query.setParameter("idCarrito", idCarrito);
            query.setParameter("idProducto", idProducto);
            ItemCarrito item = query.getSingleResult();
            return Optional.of(item);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<ItemCarrito> listarPorCarrito(Long idCarrito) {
        TypedQuery<ItemCarrito> query = em.createQuery(
            "SELECT i FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito " +
            "ORDER BY i.idItem", 
            ItemCarrito.class
        );
        query.setParameter("idCarrito", idCarrito);
        return query.getResultList();
    }
    
    @Override
    public boolean eliminar(Long id) {
        try {
            ItemCarrito item = em.find(ItemCarrito.class, id);
            if (item != null) {
                em.remove(item);
                em.flush();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public int eliminarPorCarrito(Long idCarrito) {
        try {
            int deleted = em.createQuery(
                "DELETE FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito"
            )
            .setParameter("idCarrito", idCarrito)
            .executeUpdate();
            em.flush();
            return deleted;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public Long contarPorCarrito(Long idCarrito) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(i) FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito", 
            Long.class
        );
        query.setParameter("idCarrito", idCarrito);
        return query.getSingleResult();
    }
    
    @Override
    public boolean existeProductoEnCarrito(Long idCarrito, Long idProducto) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(i) FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito " +
            "AND i.producto.idProducto = :idProducto", 
            Long.class
        );
        query.setParameter("idCarrito", idCarrito);
        query.setParameter("idProducto", idProducto);
        return query.getSingleResult() > 0;
    }
}