package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IOrdenDAO;
import com.marketplace.entities.Orden;
import com.marketplace.enums.EstadoOrden;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para Orden
 * @author Felipe Moreno
 */
@Stateless
public class OrdenDAOImpl implements IOrdenDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Orden crear(Orden orden) {
        em.persist(orden);
        em.flush();
        return orden;
    }
    
    @Override
    public Orden actualizar(Orden orden) {
        Orden updated = em.merge(orden);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Orden> buscarPorId(Long id) {
        try {
            Orden orden = em.find(Orden.class, id);
            return Optional.ofNullable(orden);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Orden> buscarPorNumeroOrden(String numeroOrden) {
        try {
            TypedQuery<Orden> query = em.createQuery(
                "SELECT o FROM Orden o WHERE o.numeroOrden = :numeroOrden", 
                Orden.class
            );
            query.setParameter("numeroOrden", numeroOrden);
            Orden orden = query.getSingleResult();
            return Optional.of(orden);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Orden> listarTodas() {
        TypedQuery<Orden> query = em.createQuery(
            "SELECT o FROM Orden o ORDER BY o.fechaOrden DESC", 
            Orden.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Orden> listarPorUsuario(Long idUsuario) {
        TypedQuery<Orden> query = em.createQuery(
            "SELECT o FROM Orden o WHERE o.usuario.idUsuario = :idUsuario " +
            "ORDER BY o.fechaOrden DESC", 
            Orden.class
        );
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }
    
    @Override
    public List<Orden> listarPorEstado(EstadoOrden estado) {
        TypedQuery<Orden> query = em.createQuery(
            "SELECT o FROM Orden o WHERE o.estado = :estado " +
            "ORDER BY o.fechaOrden DESC", 
            Orden.class
        );
        query.setParameter("estado", estado);
        return query.getResultList();
    }
    
    @Override
    public List<Orden> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        TypedQuery<Orden> query = em.createQuery(
            "SELECT o FROM Orden o WHERE o.fechaOrden BETWEEN :inicio AND :fin " +
            "ORDER BY o.fechaOrden DESC", 
            Orden.class
        );
        query.setParameter("inicio", fechaInicio);
        query.setParameter("fin", fechaFin);
        return query.getResultList();
    }
    
    @Override
    public List<Orden> listarVentasPorVendedor(Long idVendedor) {
        TypedQuery<Orden> query = em.createQuery(
            "SELECT DISTINCT o FROM Orden o JOIN o.detalles d " +
            "WHERE d.producto.vendedor.idUsuario = :idVendedor " +
            "ORDER BY o.fechaOrden DESC", 
            Orden.class
        );
        query.setParameter("idVendedor", idVendedor);
        return query.getResultList();
    }
    
    @Override
    public boolean cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        try {
            int updated = em.createQuery(
                "UPDATE Orden o SET o.estado = :estado WHERE o.idOrden = :id"
            )
            .setParameter("estado", nuevoEstado)
            .setParameter("id", id)
            .executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean perteneceAUsuario(Long idOrden, Long idUsuario) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(o) FROM Orden o WHERE o.idOrden = :idOrden " +
            "AND o.usuario.idUsuario = :idUsuario", 
            Long.class
        );
        query.setParameter("idOrden", idOrden);
        query.setParameter("idUsuario", idUsuario);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public Long contarPorUsuario(Long idUsuario) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(o) FROM Orden o WHERE o.usuario.idUsuario = :idUsuario", 
            Long.class
        );
        query.setParameter("idUsuario", idUsuario);
        return query.getSingleResult();
    }
    
    @Override
    public Long contarPorEstado(EstadoOrden estado) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(o) FROM Orden o WHERE o.estado = :estado", 
            Long.class
        );
        query.setParameter("estado", estado);
        return query.getSingleResult();
    }
    
    @Override
    public Double obtenerTotalVentasPorVendedor(Long idVendedor, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        TypedQuery<Double> query = em.createQuery(
            "SELECT COALESCE(SUM(d.subtotal), 0.0) FROM DetalleOrden d " +
            "WHERE d.producto.vendedor.idUsuario = :idVendedor " +
            "AND d.orden.fechaOrden BETWEEN :inicio AND :fin " +
            "AND d.orden.estado = :estado", 
            Double.class
        );
        query.setParameter("idVendedor", idVendedor);
        query.setParameter("inicio", fechaInicio);
        query.setParameter("fin", fechaFin);
        query.setParameter("estado", EstadoOrden.CONFIRMADA);
        return query.getSingleResult();
    }
}