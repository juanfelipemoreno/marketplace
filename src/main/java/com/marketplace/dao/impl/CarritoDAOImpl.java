package com.marketplace.dao.impl;

import com.marketplace.dao.interfaces.ICarritoDAO;
import com.marketplace.entities.Carrito;
import com.marketplace.entities.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

/**
 * Implementación DAO para Carrito
 * @author Felipe Moreno
 */
@Stateless
public class CarritoDAOImpl implements ICarritoDAO {
    
    @PersistenceContext(unitName = "MarketplacePU")
    private EntityManager em;
    
    @Override
    public Carrito crear(Carrito carrito) {
        em.persist(carrito);
        em.flush();
        return carrito;
    }
    
    @Override
    public Carrito actualizar(Carrito carrito) {
        Carrito updated = em.merge(carrito);
        em.flush();
        return updated;
    }
    
    @Override
    public Optional<Carrito> buscarPorId(Long id) {
        try {
            Carrito carrito = em.find(Carrito.class, id);
            return Optional.ofNullable(carrito);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Carrito> buscarPorUsuario(Long idUsuario) {
        try {
            TypedQuery<Carrito> query = em.createQuery(
                "SELECT c FROM Carrito c WHERE c.usuario.idUsuario = :idUsuario AND c.estado = true", 
                Carrito.class
            );
            query.setParameter("idUsuario", idUsuario);
            Carrito carrito = query.getSingleResult();
            return Optional.of(carrito);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Carrito obtenerOCrearCarrito(Long idUsuario) {
        Optional<Carrito> carritoExistente = buscarPorUsuario(idUsuario);
        
        if (carritoExistente.isPresent()) {
            return carritoExistente.get();
        }
        
        // Crear nuevo carrito
        Usuario usuario = em.find(Usuario.class, idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario);
        }
        
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setEstado(true);
        
        return crear(nuevoCarrito);
    }
    
    @Override
    public boolean eliminar(Long id) {
        try {
            Carrito carrito = em.find(Carrito.class, id);
            if (carrito != null) {
                em.remove(carrito);
                em.flush();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean limpiarItems(Long idCarrito) {
        try {
            int deleted = em.createQuery(
                "DELETE FROM ItemCarrito i WHERE i.carrito.idCarrito = :idCarrito"
            )
            .setParameter("idCarrito", idCarrito)
            .executeUpdate();
            em.flush();
            return deleted >= 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean perteneceAUsuario(Long idCarrito, Long idUsuario) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Carrito c WHERE c.idCarrito = :idCarrito " +
            "AND c.usuario.idUsuario = :idUsuario", 
            Long.class
        );
        query.setParameter("idCarrito", idCarrito);
        query.setParameter("idUsuario", idUsuario);
        return query.getSingleResult() > 0;
    }
}