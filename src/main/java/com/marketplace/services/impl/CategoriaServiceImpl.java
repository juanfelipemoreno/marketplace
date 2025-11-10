package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.ICategoriaDAO;
import com.marketplace.dto.categoria.*;
import com.marketplace.entities.Categoria;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.CategoriaMapper;
import com.marketplace.services.interfaces.ICategoriaService;
import com.marketplace.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Categoría
 * @author Felipe Moreno
 */
@Stateless
public class CategoriaServiceImpl implements ICategoriaService {
    
    @EJB
    private ICategoriaDAO categoriaDAO;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CategoriaResponseDTO crear(CategoriaDTO dto) {
        // Validaciones
        validarDatos(dto);
        
        // Verificar nombre único
        if (categoriaDAO.existeNombre(dto.getNombre())) {
            throw new ValidationException("Ya existe una categoría con ese nombre");
        }
        
        // Crear categoría
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria creada = categoriaDAO.crear(categoria);
        
        return CategoriaMapper.toResponseDTO(creada);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CategoriaResponseDTO actualizar(Long idCategoria, CategoriaDTO dto) {
        // Validaciones
        validarDatos(dto);
        
        // Buscar categoría
        Categoria categoria = buscarCategoriaOExcepcion(idCategoria);
        
        // Verificar nombre único (excluyendo la categoría actual)
        if (dto.getNombre() != null && !dto.getNombre().equals(categoria.getNombre())) {
            if (categoriaDAO.existeNombreExcluyendo(dto.getNombre(), idCategoria)) {
                throw new ValidationException("Ya existe otra categoría con ese nombre");
            }
        }
        
        // Actualizar
        CategoriaMapper.updateEntityFromDTO(categoria, dto);
        Categoria actualizada = categoriaDAO.actualizar(categoria);
        
        return CategoriaMapper.toResponseDTO(actualizada);
    }
    
    @Override
    public CategoriaResponseDTO obtenerPorId(Long idCategoria) {
        Categoria categoria = buscarCategoriaOExcepcion(idCategoria);
        return CategoriaMapper.toResponseDTO(categoria);
    }
    
    @Override
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaDAO.listarTodas().stream()
                .map(CategoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CategoriaResponseDTO> listarActivas() {
        return categoriaDAO.listarActivas().stream()
                .map(CategoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CategoriaResponseDTO cambiarEstado(Long idCategoria, boolean estado) {
        Categoria categoria = buscarCategoriaOExcepcion(idCategoria);
        categoria.setEstado(estado);
        Categoria actualizada = categoriaDAO.actualizar(categoria);
        return CategoriaMapper.toResponseDTO(actualizada);
    }
    
    @Override
    public boolean existeNombre(String nombre) {
        return categoriaDAO.existeNombre(nombre);
    }
    
    // Métodos auxiliares privados
    
    private void validarDatos(CategoriaDTO dto) {
        if (!ValidationUtil.isNotEmpty(dto.getNombre())) {
            throw new ValidationException("El nombre de la categoría es obligatorio");
        }
        
        if (!ValidationUtil.hasLengthInRange(dto.getNombre(), 3, 100)) {
            throw new ValidationException("El nombre debe tener entre 3 y 100 caracteres");
        }
    }
    
    private Categoria buscarCategoriaOExcepcion(Long id) {
        return categoriaDAO.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Categoría", id));
    }
}