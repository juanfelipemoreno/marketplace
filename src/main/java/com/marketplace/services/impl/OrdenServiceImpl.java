package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.*;
import com.marketplace.dto.orden.*;
import com.marketplace.entities.*;
import com.marketplace.enums.*;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.OrdenMapper;
import com.marketplace.services.interfaces.IOrdenService;
import com.marketplace.util.TokenGenerator;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Orden
 * @author Felipe Moreno
 */
@Stateless
public class OrdenServiceImpl implements IOrdenService {
    
    @EJB
    private IOrdenDAO ordenDAO;
    
    @EJB
    private IDetalleOrdenDAO detalleOrdenDAO;
    
    @EJB
    private ICarritoDAO carritoDAO;
    
    @EJB
    private IItemCarritoDAO itemCarritoDAO;
    
    @EJB
    private IUsuarioDAO usuarioDAO;
    
    @EJB
    private IInventarioFisicoDAO inventarioFisicoDAO;
    
    @EJB
    private IInventarioDigitalDAO inventarioDigitalDAO;
    
    @EJB
    private IDescargaDAO descargaDAO;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OrdenResponseDTO procesarCompra(Long idUsuario, OrdenCreacionDTO dto) {
        // Buscar usuario
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario", idUsuario));
        
        // Buscar carrito
        Carrito carrito = carritoDAO.buscarPorUsuario(idUsuario)
                .orElseThrow(() -> new BusinessException("No tiene items en el carrito"));
        
        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("El carrito está vacío");
        }
        
        // Validar disponibilidad de todos los items
        validarDisponibilidadItems(carrito);
        
        // Crear orden
        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setNumeroOrden(TokenGenerator.generateOrderNumber());
        orden.setMetodoPago(dto.getMetodoPago());
        orden.setEstado(EstadoOrden.CONFIRMADA);
        
        // Calcular total
        BigDecimal total = BigDecimal.ZERO;
        
        // Guardar orden primero para obtener el ID
        Orden ordenCreada = ordenDAO.crear(orden);
        
        // Crear detalles de orden y actualizar inventarios
        for (ItemCarrito item : carrito.getItems()) {
            // Crear detalle
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(ordenCreada);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            
            BigDecimal subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            detalle.setSubtotal(subtotal);
            
            detalleOrdenDAO.crear(detalle);
            
            total = total.add(subtotal);
            
            // Actualizar inventario
            actualizarInventario(item.getProducto(), item.getCantidad());
            
            // Si es producto digital, crear registro de descarga
            if (item.getProducto().getTipoProducto() == TipoProducto.DIGITAL) {
                crearRegistroDescarga(usuario, item.getProducto(), ordenCreada);
            }
        }
        
        // Actualizar total de la orden
        ordenCreada.setTotal(total);
        ordenDAO.actualizar(ordenCreada);
        
        // Vaciar carrito
        carritoDAO.limpiarItems(carrito.getIdCarrito());
        
        return OrdenMapper.toResponseDTO(ordenCreada);
    }
    
    @Override
    public OrdenResponseDTO obtenerOrden(Long idOrden, Long idUsuario) {
        Orden orden = ordenDAO.buscarPorId(idOrden)
                .orElseThrow(() -> new NotFoundException("Orden", idOrden));
        
        // Verificar que la orden pertenece al usuario
        if (!orden.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new BusinessException("No tiene permisos para ver esta orden");
        }
        
        return OrdenMapper.toResponseDTO(orden);
    }
    
    @Override
    public OrdenResponseDTO obtenerPorNumeroOrden(String numeroOrden, Long idUsuario) {
        Orden orden = ordenDAO.buscarPorNumeroOrden(numeroOrden)
                .orElseThrow(() -> new NotFoundException("Orden con número: " + numeroOrden));
        
        // Verificar que la orden pertenece al usuario
        if (!orden.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new BusinessException("No tiene permisos para ver esta orden");
        }
        
        return OrdenMapper.toResponseDTO(orden);
    }
    
    @Override
    public List<OrdenResumenDTO> listarMisOrdenes(Long idUsuario) {
        return ordenDAO.listarPorUsuario(idUsuario).stream()
                .map(OrdenMapper::toResumenDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrdenResumenDTO> listarMisVentas(Long idVendedor) {
        return ordenDAO.listarVentasPorVendedor(idVendedor).stream()
                .map(OrdenMapper::toResumenDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrdenResumenDTO> listarTodas() {
        return ordenDAO.listarTodas().stream()
                .map(OrdenMapper::toResumenDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean cancelarOrden(Long idOrden, Long idUsuario) {
        Orden orden = ordenDAO.buscarPorId(idOrden)
                .orElseThrow(() -> new NotFoundException("Orden", idOrden));
        
        // Verificar que la orden pertenece al usuario
        if (!orden.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new BusinessException("No tiene permisos para cancelar esta orden");
        }
        
        // Verificar que la orden puede ser cancelada
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new BusinessException("La orden ya está cancelada");
        }
        
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new BusinessException("No se puede cancelar una orden completada");
        }
        
        // Restaurar inventario
        for (DetalleOrden detalle : orden.getDetalles()) {
            restaurarInventario(detalle.getProducto(), detalle.getCantidad());
        }
        
        // Cambiar estado
        return ordenDAO.cambiarEstado(idOrden, EstadoOrden.CANCELADA);
    }
    
    // Métodos auxiliares privados
    
    private void validarDisponibilidadItems(Carrito carrito) {
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            
            if (producto.getTipoProducto() == TipoProducto.FISICO) {
                if (!inventarioFisicoDAO.verificarDisponibilidad(
                        producto.getIdProducto(), item.getCantidad())) {
                    throw new InsufficientStockException(
                        producto.getIdProducto(),
                        producto.getNombre(),
                        item.getCantidad(),
                        inventarioFisicoDAO.obtenerCantidadDisponible(producto.getIdProducto())
                    );
                }
            } else if (producto.getTipoProducto() == TipoProducto.DIGITAL) {
                if (!inventarioDigitalDAO.verificarDisponibilidad(
                        producto.getIdProducto(), item.getCantidad())) {
                    throw new InsufficientStockException(
                        producto.getIdProducto(),
                        producto.getNombre(),
                        item.getCantidad(),
                        inventarioDigitalDAO.obtenerLicenciasDisponibles(producto.getIdProducto())
                    );
                }
            }
        }
    }
    
    private void actualizarInventario(Producto producto, Integer cantidad) {
        if (producto.getTipoProducto() == TipoProducto.FISICO) {
            boolean actualizado = inventarioFisicoDAO.reducirCantidad(
                producto.getIdProducto(), cantidad
            );
            
            if (!actualizado) {
                throw new BusinessException(
                    "Error al actualizar inventario del producto: " + producto.getNombre()
                );
            }
        } else if (producto.getTipoProducto() == TipoProducto.DIGITAL) {
            boolean actualizado = inventarioDigitalDAO.reducirLicencias(
                producto.getIdProducto(), cantidad
            );
            
            if (!actualizado) {
                throw new BusinessException(
                    "Error al actualizar licencias del producto: " + producto.getNombre()
                );
            }
        }
    }
    
    private void restaurarInventario(Producto producto, Integer cantidad) {
        if (producto.getTipoProducto() == TipoProducto.FISICO) {
            inventarioFisicoDAO.aumentarCantidad(producto.getIdProducto(), cantidad);
        } else if (producto.getTipoProducto() == TipoProducto.DIGITAL) {
            inventarioDigitalDAO.aumentarLicencias(producto.getIdProducto(), cantidad);
        }
    }
    
    private void crearRegistroDescarga(Usuario usuario, Producto producto, Orden orden) {
        Descarga descarga = new Descarga();
        descarga.setUsuario(usuario);
        descarga.setProducto(producto);
        descarga.setOrden(orden);
        descarga.setNumeroDescargas(0);
        descarga.setLimiteDescargas(3); // Límite por defecto
        descargaDAO.crear(descarga);
    }
}