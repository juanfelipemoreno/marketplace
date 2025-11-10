package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.*;
import com.marketplace.dto.producto.*;
import com.marketplace.entities.*;
import com.marketplace.enums.TipoProducto;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.ProductoMapper;
import com.marketplace.services.interfaces.IProductoService;
import com.marketplace.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Producto
 * @author Felipe Moreno
 */
@Stateless
public class ProductoServiceImpl implements IProductoService {
    
    @EJB
    private IProductoDAO productoDAO;
    
    @EJB
    private IUsuarioDAO usuarioDAO;
    
    @EJB
    private ICategoriaDAO categoriaDAO;
    
    @EJB
    private IInventarioFisicoDAO inventarioFisicoDAO;
    
    @EJB
    private IInventarioDigitalDAO inventarioDigitalDAO;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ProductoResponseDTO crear(ProductoCreacionDTO dto, Long idVendedor) {
        // Validaciones
        validarDatosProducto(dto);
        
        // Buscar vendedor y categoría
        Usuario vendedor = usuarioDAO.buscarPorId(idVendedor)
                .orElseThrow(() -> new NotFoundException("Usuario", idVendedor));
        
        Categoria categoria = categoriaDAO.buscarPorId(dto.getIdCategoria())
                .orElseThrow(() -> new NotFoundException("Categoría", dto.getIdCategoria()));
        
        // Crear producto
        Producto producto = ProductoMapper.toEntity(dto, vendedor, categoria);
        Producto productoCreado = productoDAO.crear(producto);
        
        // Crear inventario según tipo
        if (dto.getTipoProducto().equals("FISICO")) {
            crearInventarioFisico(productoCreado, dto.getCantidadDisponible());
        } else if (dto.getTipoProducto().equals("DIGITAL")) {
            crearInventarioDigital(productoCreado, dto);
        }
        
        return ProductoMapper.toResponseDTO(productoCreado);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ProductoResponseDTO actualizar(Long idProducto, ProductoCreacionDTO dto, Long idVendedor) {
        // Validaciones
        validarDatosProducto(dto);
        
        // Buscar producto y verificar propiedad
        Producto producto = buscarProductoOExcepcion(idProducto);
        
        if (!producto.getVendedor().getIdUsuario().equals(idVendedor)) {
            throw new BusinessException("No tiene permisos para editar este producto");
        }
        
        // Actualizar datos básicos
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaDAO.buscarPorId(dto.getIdCategoria())
                    .orElseThrow(() -> new NotFoundException("Categoría", dto.getIdCategoria()));
            producto.setCategoria(categoria);
        }
        
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getImagenUrl() != null) producto.setImagenUrl(dto.getImagenUrl());
        
        // Actualizar inventario si se proporciona
        if (producto.getTipoProducto() == TipoProducto.FISICO && dto.getCantidadDisponible() != null) {
            InventarioFisico inventario = producto.getInventarioFisico();
            if (inventario != null) {
                inventario.setCantidadDisponible(dto.getCantidadDisponible());
                inventarioFisicoDAO.actualizar(inventario);
            }
        } else if (producto.getTipoProducto() == TipoProducto.DIGITAL) {
            InventarioDigital inventario = producto.getInventarioDigital();
            if (inventario != null) {
                if (dto.getLicenciasTotales() != null) {
                    inventario.setLicenciasTotales(dto.getLicenciasTotales());
                    inventario.setLicenciasDisponibles(dto.getLicenciasTotales());
                }
                if (dto.getArchivoUrl() != null) inventario.setArchivoUrl(dto.getArchivoUrl());
                if (dto.getTamanoArchivo() != null) inventario.setTamanoArchivo(dto.getTamanoArchivo());
                inventarioDigitalDAO.actualizar(inventario);
            }
        }
        
        Producto actualizado = productoDAO.actualizar(producto);
        return ProductoMapper.toResponseDTO(actualizado);
    }
    
    @Override
    public ProductoResponseDTO obtenerPorId(Long idProducto) {
        Producto producto = buscarProductoOExcepcion(idProducto);
        return ProductoMapper.toResponseDTO(producto);
    }
    
    @Override
    public List<ProductoResponseDTO> listarMisProductos(Long idVendedor) {
        return productoDAO.listarPorVendedor(idVendedor).stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean cambiarEstado(Long idProducto, Long idVendedor, boolean estado) {
        Producto producto = buscarProductoOExcepcion(idProducto);
        
        if (!producto.getVendedor().getIdUsuario().equals(idVendedor)) {
            throw new BusinessException("No tiene permisos para modificar este producto");
        }
        
        return productoDAO.cambiarEstado(idProducto, estado);
    }
    
    @Override
    public List<ProductoCatalogoDTO> listarCatalogo() {
        return productoDAO.listarDisponibles().stream()
                .map(ProductoMapper::toCatalogoDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductoCatalogoDTO> buscarProductos(String termino) {
        return productoDAO.buscarPorTermino(termino).stream()
                .map(ProductoMapper::toCatalogoDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductoCatalogoDTO> listarPorCategoria(Long idCategoria) {
        return productoDAO.listarPorCategoriaActivos(idCategoria).stream()
                .map(ProductoMapper::toCatalogoDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductoCatalogoDTO> listarPorTipo(TipoProducto tipo) {
        return productoDAO.listarPorTipo(tipo).stream()
                .map(ProductoMapper::toCatalogoDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductoCatalogoDTO obtenerDetalleCatalogo(Long idProducto) {
        Producto producto = buscarProductoOExcepcion(idProducto);
        
        if (!producto.getEstado()) {
            throw new BusinessException("El producto no está disponible");
        }
        
        return ProductoMapper.toCatalogoDTO(producto);
    }
    
    @Override
    public boolean perteneceAVendedor(Long idProducto, Long idVendedor) {
        return productoDAO.perteneceAVendedor(idProducto, idVendedor);
    }
    
    // Métodos auxiliares privados
    
    private void validarDatosProducto(ProductoCreacionDTO dto) {
        if (!ValidationUtil.isNotEmpty(dto.getNombre())) {
            throw new ValidationException("El nombre del producto es obligatorio");
        }
        
        if (!ValidationUtil.isPositive(dto.getPrecio())) {
            throw new ValidationException("El precio debe ser mayor a 0");
        }
        
        if (!ValidationUtil.isValidId(dto.getIdCategoria())) {
            throw new ValidationException("Debe seleccionar una categoría válida");
        }
        
        if (!"FISICO".equals(dto.getTipoProducto()) && !"DIGITAL".equals(dto.getTipoProducto())) {
            throw new ValidationException("Tipo de producto inválido");
        }
        
        // Validaciones específicas por tipo
        if ("FISICO".equals(dto.getTipoProducto())) {
            if (dto.getCantidadDisponible() == null || dto.getCantidadDisponible() < 0) {
                throw new ValidationException("La cantidad disponible debe ser mayor o igual a 0");
            }
        } else if ("DIGITAL".equals(dto.getTipoProducto())) {
            if (dto.getLicenciasTotales() == null || dto.getLicenciasTotales() <= 0) {
                throw new ValidationException("El número de licencias debe ser mayor a 0");
            }
            if (!ValidationUtil.isNotEmpty(dto.getArchivoUrl())) {
                throw new ValidationException("Debe proporcionar la URL del archivo digital");
            }
        }
    }
    
    private void crearInventarioFisico(Producto producto, Integer cantidadInicial) {
        InventarioFisico inventario = new InventarioFisico();
        inventario.setProducto(producto);
        inventario.setCantidadDisponible(cantidadInicial != null ? cantidadInicial : 0);
        inventario.setCantidadReservada(0);
        inventarioFisicoDAO.crear(inventario);
        producto.setInventarioFisico(inventario);
    }
    
    private void crearInventarioDigital(Producto producto, ProductoCreacionDTO dto) {
        InventarioDigital inventario = new InventarioDigital();
        inventario.setProducto(producto);
        inventario.setLicenciasTotales(dto.getLicenciasTotales());
        inventario.setLicenciasDisponibles(dto.getLicenciasTotales());
        inventario.setArchivoUrl(dto.getArchivoUrl());
        inventario.setTamanoArchivo(dto.getTamanoArchivo());
        inventarioDigitalDAO.crear(inventario);
        producto.setInventarioDigital(inventario);
    }
    
    private Producto buscarProductoOExcepcion(Long id) {
        return productoDAO.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Producto", id));
    }
}