package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IDescargaDAO;
import com.marketplace.entities.Descarga;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para Descarga
 * @author Felipe Moreno
 */
@Stateless
public class DescargaDAOImpl implements IDescargaDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Descarga crear(Descarga descarga) {
        em.persist(descarga);
        em.flush();
        return descarga;
    }
    
    @Override
    public Descarga actualizar(Descarga descarga) {
        Descarga updated = em.merge(descarga);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Descarga> buscarPorId(Long id) {
        try {
            Descarga descarga = em.find(Descarga.class, id);
            return Optional.ofNullable(descarga);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Descarga> buscarPorUsuarioYProducto(Long idUsuario, Long idProducto) {
        try {
            TypedQuery<Descarga> query = em.createQuery(
                "SELECT d FROM Descarga d WHERE d.usuario.idUsuario = :idUsuario " +
                "AND d.producto.idProducto = :idProducto", 
                Descarga.class
            );
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("idProducto", idProducto);
            Descarga descarga = query.getSingleResult();
            return Optional.of(descarga);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Descarga> listarPorUsuario(Long idUsuario) {
        TypedQuery<Descarga> query = em.createQuery(
            "SELECT d FROM Descarga d WHERE d.usuario.idUsuario = :idUsuario " +
            "ORDER BY d.fechaDescarga DESC", 
            Descarga.class
        );
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }
    
    @Override
    public List<Descarga> listarPorProducto(Long idProducto) {
        TypedQuery<Descarga> query = em.createQuery(
            "SELECT d FROM Descarga d WHERE d.producto.idProducto = :idProducto " +
            "ORDER BY d.fechaDescarga DESC", 
            Descarga.class
        );
        query.setParameter("idProducto", idProducto);
        return query.getResultList();
    }
    
    @Override
    public boolean incrementarContador(Long id) {
        try {
            int updated = em.createQuery(
                "UPDATE Descarga d SET d.numeroDescargas = d.numeroDescargas + 1 " +
                "WHERE d.idDescarga = :id"
            )
            .setParameter("id", id)
            .executeUpdate();
            
            em.flush();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean puedeDescargar(Long idUsuario, Long idProducto) {
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Descarga d WHERE d.usuario.idUsuario = :idUsuario " +
                "AND d.producto.idProducto = :idProducto " +
                "AND d.numeroDescargas < d.limiteDescargas", 
                Long.class
            );
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("idProducto", idProducto);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean haComprado(Long idUsuario, Long idProducto) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(d) FROM Descarga d WHERE d.usuario.idUsuario = :idUsuario " +
            "AND d.producto.idProducto = :idProducto", 
            Long.class
        );
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idProducto", idProducto);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public List<Descarga> obtenerDescargasDisponibles(Long idUsuario) {
        TypedQuery<Descarga> query = em.createQuery(
            "SELECT d FROM Descarga d WHERE d.usuario.idUsuario = :idUsuario " +
            "AND d.numeroDescargas < d.limiteDescargas " +
            "ORDER BY d.fechaDescarga DESC", 
            Descarga.class
        );
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }
    
    @Override
    public Long contarDescargasPorProducto(Long idProducto) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COALESCE(SUM(d.numeroDescargas), 0) FROM Descarga d " +
            "WHERE d.producto.idProducto = :idProducto", 
            Long.class
        );
        query.setParameter("idProducto", idProducto);
        return query.getSingleResult();
    }
}