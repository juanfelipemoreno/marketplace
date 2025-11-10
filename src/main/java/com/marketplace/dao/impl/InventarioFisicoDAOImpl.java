package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IInventarioFisicoDAO;
import com.marketplace.entities.InventarioFisico;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para InventarioFisico
 * @author Felipe Moreno
 */
@Stateless
public class InventarioFisicoDAOImpl implements IInventarioFisicoDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public InventarioFisico crear(InventarioFisico inventario) {
        em.persist(inventario);
        em.flush();
        return inventario;
    }
    
    @Override
    public InventarioFisico actualizar(InventarioFisico inventario) {
        InventarioFisico updated = em.merge(inventario);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<InventarioFisico> buscarPorId(Long id) {
        try {
            InventarioFisico inventario = em.find(InventarioFisico.class, id);
            return Optional.ofNullable(inventario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<InventarioFisico> buscarPorProducto(Long idProducto) {
        try {
            TypedQuery<InventarioFisico> query = em.createQuery(
                "SELECT i FROM InventarioFisico i WHERE i.producto.idProducto = :idProducto", 
                InventarioFisico.class
            );
            query.setParameter("idProducto", idProducto);
            InventarioFisico inventario = query.getSingleResult();
            return Optional.of(inventario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public boolean reducirCantidad(Long idProducto, Integer cantidad) {
        try {
            int updated = em.createQuery(
                "UPDATE InventarioFisico i SET i.cantidadDisponible = i.cantidadDisponible - :cantidad " +
                "WHERE i.producto.idProducto = :idProducto AND i.cantidadDisponible >= :cantidad"
            )
            .setParameter("cantidad", cantidad)
            .setParameter("idProducto", idProducto)
            .executeUpdate();
            
            em.flush();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean aumentarCantidad(Long idProducto, Integer cantidad) {
        try {
            int updated = em.createQuery(
                "UPDATE InventarioFisico i SET i.cantidadDisponible = i.cantidadDisponible + :cantidad " +
                "WHERE i.producto.idProducto = :idProducto"
            )
            .setParameter("cantidad", cantidad)
            .setParameter("idProducto", idProducto)
            .executeUpdate();
            
            em.flush();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean verificarDisponibilidad(Long idProducto, Integer cantidadRequerida) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(i) FROM InventarioFisico i WHERE i.producto.idProducto = :idProducto " +
            "AND i.cantidadDisponible >= :cantidad", 
            Long.class
        );
        query.setParameter("idProducto", idProducto);
        query.setParameter("cantidad", cantidadRequerida);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public List<InventarioFisico> listarConStockBajo() {
        TypedQuery<InventarioFisico> query = em.createQuery(
            "SELECT i FROM InventarioFisico i WHERE i.cantidadDisponible < 5 " +
            "AND i.cantidadDisponible > 0 ORDER BY i.cantidadDisponible", 
            InventarioFisico.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<InventarioFisico> listarSinStock() {
        TypedQuery<InventarioFisico> query = em.createQuery(
            "SELECT i FROM InventarioFisico i WHERE i.cantidadDisponible = 0", 
            InventarioFisico.class
        );
        return query.getResultList();
    }
    
    @Override
    public Integer obtenerCantidadDisponible(Long idProducto) {
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT i.cantidadDisponible FROM InventarioFisico i " +
                "WHERE i.producto.idProducto = :idProducto", 
                Integer.class
            );
            query.setParameter("idProducto", idProducto);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }
}