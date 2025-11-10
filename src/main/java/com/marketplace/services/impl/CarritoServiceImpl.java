package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.*;
import com.marketplace.dto.carrito.*;
import com.marketplace.entities.*;
import com.marketplace.enums.TipoProducto;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.CarritoMapper;
import com.marketplace.services.interfaces.ICarritoService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.Optional;

/**
 * Implementación del servicio de Carrito
 * @author Felipe Moreno
 */
@Stateless
public class CarritoServiceImpl implements ICarritoService {
    
    @EJB
    private ICarritoDAO carritoDAO;
    
    @EJB
    private IItemCarritoDAO itemCarritoDAO;
    
    @EJB
    private IProductoDAO productoDAO;
    
    @EJB
    private IInventarioFisicoDAO inventarioFisicoDAO;
    
    @EJB
    private IInventarioDigitalDAO inventarioDigitalDAO;
    
    @Override
    public CarritoResponseDTO obtenerCarrito(Long idUsuario) {
        Carrito carrito = carritoDAO.obtenerOCrearCarrito(idUsuario);
        return CarritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CarritoResponseDTO agregarItem(Long idUsuario, ItemCarritoDTO dto) {
        // Validar datos
        if (dto.getIdProducto() == null || dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new ValidationException("Datos del item inválidos");
        }
        
        // Buscar producto
        Producto producto = productoDAO.buscarPorId(dto.getIdProducto())
                .orElseThrow(() -> new NotFoundException("Producto", dto.getIdProducto()));
        
        // Verificar que el producto esté activo
        if (!producto.getEstado()) {
            throw new BusinessException("El producto no está disponible");
        }
        
        // Verificar disponibilidad
        verificarDisponibilidad(producto, dto.getCantidad());
        
        // Obtener o crear carrito
        Carrito carrito = carritoDAO.obtenerOCrearCarrito(idUsuario);
        
        // Verificar si el producto ya está en el carrito
        Optional<ItemCarrito> itemExistente = itemCarritoDAO.buscarPorCarritoYProducto(
            carrito.getIdCarrito(), 
            dto.getIdProducto()
        );
        
        if (itemExistente.isPresent()) {
            // Actualizar cantidad
            ItemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + dto.getCantidad();
            
            // Verificar disponibilidad con la nueva cantidad
            verificarDisponibilidad(producto, nuevaCantidad);
            
            item.setCantidad(nuevaCantidad);
            itemCarritoDAO.actualizar(item);
        } else {
            // Crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(dto.getCantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            itemCarritoDAO.crear(nuevoItem);
        }
        
        // Refrescar carrito
        Carrito carritoActualizado = carritoDAO.buscarPorId(carrito.getIdCarrito()).get();
        return CarritoMapper.toResponseDTO(carritoActualizado);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idItem, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new ValidationException("La cantidad debe ser mayor a 0");
        }
        
        // Buscar item
        ItemCarrito item = itemCarritoDAO.buscarPorId(idItem)
                .orElseThrow(() -> new NotFoundException("Item del carrito", idItem));
        
        // Verificar que el item pertenece al carrito del usuario
        if (!carritoDAO.perteneceAUsuario(item.getCarrito().getIdCarrito(), idUsuario)) {
            throw new BusinessException("El item no pertenece a su carrito");
        }
        
        // Verificar disponibilidad
        verificarDisponibilidad(item.getProducto(), nuevaCantidad);
        
        // Actualizar cantidad
        item.setCantidad(nuevaCantidad);
        itemCarritoDAO.actualizar(item);
        
        // Refrescar carrito
        Carrito carrito = carritoDAO.buscarPorId(item.getCarrito().getIdCarrito()).get();
        return CarritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CarritoResponseDTO eliminarItem(Long idUsuario, Long idItem) {
        // Buscar item
        ItemCarrito item = itemCarritoDAO.buscarPorId(idItem)
                .orElseThrow(() -> new NotFoundException("Item del carrito", idItem));
        
        // Verificar que el item pertenece al carrito del usuario
        if (!carritoDAO.perteneceAUsuario(item.getCarrito().getIdCarrito(), idUsuario)) {
            throw new BusinessException("El item no pertenece a su carrito");
        }
        
        Long idCarrito = item.getCarrito().getIdCarrito();
        
        // Eliminar item
        itemCarritoDAO.eliminar(idItem);
        
        // Refrescar carrito
        Carrito carrito = carritoDAO.buscarPorId(idCarrito).get();
        return CarritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean vaciarCarrito(Long idUsuario) {
        Optional<Carrito> carritoOpt = carritoDAO.buscarPorUsuario(idUsuario);
        
        if (!carritoOpt.isPresent()) {
            return true; // No hay carrito, consideramos éxito
        }
        
        Carrito carrito = carritoOpt.get();
        return carritoDAO.limpiarItems(carrito.getIdCarrito());
    }
    
    @Override
    public boolean validarDisponibilidad(Long idUsuario) {
        Optional<Carrito> carritoOpt = carritoDAO.buscarPorUsuario(idUsuario);
        
        if (!carritoOpt.isPresent() || carritoOpt.get().getItems().isEmpty()) {
            return false;
        }
        
        Carrito carrito = carritoOpt.get();
        
        for (ItemCarrito item : carrito.getItems()) {
            try {
                verificarDisponibilidad(item.getProducto(), item.getCantidad());
            } catch (InsufficientStockException e) {
                return false;
            }
        }
        
        return true;
    }
    
    // Métodos auxiliares privados
    
    private void verificarDisponibilidad(Producto producto, Integer cantidadRequerida) {
        if (producto.getTipoProducto() == TipoProducto.FISICO) {
            Integer disponible = inventarioFisicoDAO.obtenerCantidadDisponible(producto.getIdProducto());
            
            if (disponible < cantidadRequerida) {
                throw new InsufficientStockException(
                    producto.getIdProducto(),
                    producto.getNombre(),
                    cantidadRequerida,
                    disponible
                );
            }
        } else if (producto.getTipoProducto() == TipoProducto.DIGITAL) {
            Integer disponible = inventarioDigitalDAO.obtenerLicenciasDisponibles(producto.getIdProducto());
            
            if (disponible < cantidadRequerida) {
                throw new InsufficientStockException(
                    producto.getIdProducto(),
                    producto.getNombre(),
                    cantidadRequerida,
                    disponible
                );
            }
        }
    }
}