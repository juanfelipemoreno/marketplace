package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IInventarioDigitalDAO;
import com.marketplace.entities.InventarioDigital;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para InventarioDigital
 * @author Felipe Moreno
 */
@Stateless
public class InventarioDigitalDAOImpl implements IInventarioDigitalDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public InventarioDigital crear(InventarioDigital inventario) {
        em.persist(inventario);
        em.flush();
        return inventario;
    }
    
    @Override
    public InventarioDigital actualizar(InventarioDigital inventario) {
        InventarioDigital updated = em.merge(inventario);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<InventarioDigital> buscarPorId(Long id) {
        try {
            InventarioDigital inventario = em.find(InventarioDigital.class, id);
            return Optional.ofNullable(inventario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<InventarioDigital> buscarPorProducto(Long idProducto) {
        try {
            TypedQuery<InventarioDigital> query = em.createQuery(
                "SELECT i FROM InventarioDigital i WHERE i.producto.idProducto = :idProducto", 
                InventarioDigital.class
            );
            query.setParameter("idProducto", idProducto);
            InventarioDigital inventario = query.getSingleResult();
            return Optional.of(inventario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public boolean reducirLicencias(Long idProducto, Integer cantidad) {
        try {
            int updated = em.createQuery(
                "UPDATE InventarioDigital i SET i.licenciasDisponibles = i.licenciasDisponibles - :cantidad " +
                "WHERE i.producto.idProducto = :idProducto AND i.licenciasDisponibles >= :cantidad"
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
    public boolean aumentarLicencias(Long idProducto, Integer cantidad) {
        try {
            int updated = em.createQuery(
                "UPDATE InventarioDigital i SET i.licenciasDisponibles = i.licenciasDisponibles + :cantidad, " +
                "i.licenciasTotales = i.licenciasTotales + :cantidad " +
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
            "SELECT COUNT(i) FROM InventarioDigital i WHERE i.producto.idProducto = :idProducto " +
            "AND i.licenciasDisponibles >= :cantidad", 
            Long.class
        );
        query.setParameter("idProducto", idProducto);
        query.setParameter("cantidad", cantidadRequerida);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public List<InventarioDigital> listarConLicenciasBajas() {
        TypedQuery<InventarioDigital> query = em.createQuery(
            "SELECT i FROM InventarioDigital i WHERE i.licenciasDisponibles < 5 " +
            "AND i.licenciasDisponibles > 0 ORDER BY i.licenciasDisponibles", 
            InventarioDigital.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<InventarioDigital> listarSinLicencias() {
        TypedQuery<InventarioDigital> query = em.createQuery(
            "SELECT i FROM InventarioDigital i WHERE i.licenciasDisponibles = 0", 
            InventarioDigital.class
        );
        return query.getResultList();
    }
    
    @Override
    public Integer obtenerLicenciasDisponibles(Long idProducto) {
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT i.licenciasDisponibles FROM InventarioDigital i " +
                "WHERE i.producto.idProducto = :idProducto", 
                Integer.class
            );
            query.setParameter("idProducto", idProducto);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }
    
    @Override
    public String obtenerArchivoUrl(Long idProducto) {
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT i.archivoUrl FROM InventarioDigital i " +
                "WHERE i.producto.idProducto = :idProducto", 
                String.class
            );
            query.setParameter("idProducto", idProducto);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}