package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IDetalleOrdenDAO;
import com.marketplace.entities.DetalleOrden;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para DetalleOrden
 * @author Felipe Moreno
 */
@Stateless
public class DetalleOrdenDAOImpl implements IDetalleOrdenDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public DetalleOrden crear(DetalleOrden detalle) {
        em.persist(detalle);
        em.flush();
        return detalle;
    }
    
    @Override
    public Optional<DetalleOrden> buscarPorId(Long id) {
        try {
            DetalleOrden detalle = em.find(DetalleOrden.class, id);
            return Optional.ofNullable(detalle);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<DetalleOrden> listarPorOrden(Long idOrden) {
        TypedQuery<DetalleOrden> query = em.createQuery(
            "SELECT d FROM DetalleOrden d WHERE d.orden.idOrden = :idOrden " +
            "ORDER BY d.idDetalle", 
            DetalleOrden.class
        );
        query.setParameter("idOrden", idOrden);
        return query.getResultList();
    }
    
    @Override
    public List<DetalleOrden> listarPorProducto(Long idProducto) {
        TypedQuery<DetalleOrden> query = em.createQuery(
            "SELECT d FROM DetalleOrden d WHERE d.producto.idProducto = :idProducto " +
            "ORDER BY d.orden.fechaOrden DESC", 
            DetalleOrden.class
        );
        query.setParameter("idProducto", idProducto);
        return query.getResultList();
    }
    
    @Override
    public Integer contarCantidadVendida(Long idProducto) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COALESCE(SUM(d.cantidad), 0) FROM DetalleOrden d " +
            "WHERE d.producto.idProducto = :idProducto " +
            "AND d.orden.estado = 'CONFIRMADA'", 
            Long.class
        );
        query.setParameter("idProducto", idProducto);
        Long resultado = query.getSingleResult();
        return resultado != null ? resultado.intValue() : 0;
    }
    
    @Override
    public List<Object[]> obtenerProductosMasVendidos(int limite) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT d.producto.idProducto, d.producto.nombre, SUM(d.cantidad) as totalVendido " +
            "FROM DetalleOrden d " +
            "WHERE d.orden.estado = 'CONFIRMADA' " +
            "GROUP BY d.producto.idProducto, d.producto.nombre " +
            "ORDER BY totalVendido DESC", 
            Object[].class
        );
        query.setMaxResults(limite);
        return query.getResultList();
    }
}