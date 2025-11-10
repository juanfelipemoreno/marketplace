package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.IProductoDAO;
import com.marketplace.entities.Producto;
import com.marketplace.enums.TipoProducto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementación DAO para Producto
 * @author Felipe Moreno
 */
@Stateless
public class ProductoDAOImpl implements IProductoDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Producto crear(Producto producto) {
        em.persist(producto);
        em.flush();
        return producto;
    }
    
    @Override
    public Producto actualizar(Producto producto) {
        Producto updated = em.merge(producto);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Producto> buscarPorId(Long id) {
        try {
            Producto producto = em.find(Producto.class, id);
            return Optional.ofNullable(producto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Producto> listarTodos() {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p ORDER BY p.fechaCreacion DESC", 
            Producto.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarActivos() {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.estado = true ORDER BY p.fechaCreacion DESC", 
            Producto.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarPorVendedor(Long idVendedor) {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.vendedor.idUsuario = :idVendedor ORDER BY p.fechaCreacion DESC", 
            Producto.class
        );
        query.setParameter("idVendedor", idVendedor);
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarPorCategoria(Long idCategoria) {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria ORDER BY p.nombre", 
            Producto.class
        );
        query.setParameter("idCategoria", idCategoria);
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarPorTipo(TipoProducto tipo) {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.tipoProducto = :tipo AND p.estado = true ORDER BY p.nombre", 
            Producto.class
        );
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }
    
    @Override
    public List<Producto> buscarPorTermino(String termino) {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.estado = true AND " +
            "(LOWER(p.nombre) LIKE LOWER(:termino) OR LOWER(p.descripcion) LIKE LOWER(:termino)) " +
            "ORDER BY p.nombre", 
            Producto.class
        );
        query.setParameter("termino", "%" + termino + "%");
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarDisponibles() {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.estado = true AND " +
            "((p.tipoProducto = 'FISICO' AND p.inventarioFisico.cantidadDisponible > 0) OR " +
            "(p.tipoProducto = 'DIGITAL' AND p.inventarioDigital.licenciasDisponibles > 0)) " +
            "ORDER BY p.fechaCreacion DESC", 
            Producto.class
        );
        return query.getResultList();
    }
    
    @Override
    public List<Producto> listarPorCategoriaActivos(Long idCategoria) {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria " +
            "AND p.estado = true ORDER BY p.nombre", 
            Producto.class
        );
        query.setParameter("idCategoria", idCategoria);
        return query.getResultList();
    }
    
    @Override
    public boolean cambiarEstado(Long id, boolean estado) {
        try {
            int updated = em.createQuery(
                "UPDATE Producto p SET p.estado = :estado WHERE p.idProducto = :id"
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
    public boolean perteneceAVendedor(Long idProducto, Long idVendedor) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Producto p WHERE p.idProducto = :idProducto " +
            "AND p.vendedor.idUsuario = :idVendedor", 
            Long.class
        );
        query.setParameter("idProducto", idProducto);
        query.setParameter("idVendedor", idVendedor);
        return query.getSingleResult() > 0;
    }
    
    @Override
    public List<Producto> listarConStockBajo() {
        TypedQuery<Producto> query = em.createQuery(
            "SELECT p FROM Producto p WHERE p.tipoProducto = 'FISICO' " +
            "AND p.inventarioFisico.cantidadDisponible < 5 AND p.inventarioFisico.cantidadDisponible > 0 " +
            "ORDER BY p.inventarioFisico.cantidadDisponible", 
            Producto.class
        );
        return query.getResultList();
    }
    
    @Override
    public Long contarPorVendedor(Long idVendedor) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Producto p WHERE p.vendedor.idUsuario = :idVendedor", 
            Long.class
        );
        query.setParameter("idVendedor", idVendedor);
        return query.getSingleResult();
    }
    
    @Override
    public Long contarPorCategoria(Long idCategoria) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Producto p WHERE p.categoria.idCategoria = :idCategoria", 
            Long.class
        );
        query.setParameter("idCategoria", idCategoria);
        return query.getSingleResult();
    }
}