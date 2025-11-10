package com.marketplace.services.impl;

import com.marketplace.dao.interfaces.IUsuarioDAO;
import com.marketplace.dto.usuario.*;
import com.marketplace.entities.Usuario;
import com.marketplace.enums.RolUsuario;
import com.marketplace.exceptions.*;
import com.marketplace.mappers.UsuarioMapper;
import com.marketplace.security.PasswordUtil;
import com.marketplace.services.interfaces.IUsuarioService;
import com.marketplace.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Usuario
 * @author Felipe Moreno
 */
@Stateless
public class UsuarioServiceImpl implements IUsuarioService {
    
    @EJB
    private IUsuarioDAO usuarioDAO;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UsuarioResponseDTO registrar(UsuarioRegistroDTO dto) {
        // Validaciones
        validarDatosRegistro(dto);
        
        // Verificar email único
        if (usuarioDAO.existeEmail(dto.getEmail())) {
            throw new ValidationException("El email ya está registrado");
        }
        
        // Crear usuario
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setPasswordHash(PasswordUtil.hashPassword(dto.getPassword()));
        usuario.setRol(RolUsuario.COMPRADOR); // Rol por defecto
        
        // Guardar
        Usuario usuarioCreado = usuarioDAO.crear(usuario);
        
        return UsuarioMapper.toResponseDTO(usuarioCreado);
    }
    
    @Override
    public UsuarioResponseDTO login(UsuarioLoginDTO dto) {
        // Validar datos
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw AuthenticationException.invalidCredentials();
        }
        
        // Buscar usuario
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(dto.getEmail());
        if (!usuarioOpt.isPresent()) {
            throw AuthenticationException.invalidCredentials();
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar estado
        if (!usuario.getEstado()) {
            throw AuthenticationException.inactiveUser();
        }
        
        // Verificar contraseña
        if (!PasswordUtil.checkPassword(dto.getPassword(), usuario.getPasswordHash())) {
            throw AuthenticationException.invalidCredentials();
        }
        
        return UsuarioMapper.toResponseDTO(usuario);
    }
    
    @Override
    public UsuarioResponseDTO obtenerPerfil(Long idUsuario) {
        Usuario usuario = buscarUsuarioOExcepcion(idUsuario);
        return UsuarioMapper.toResponseDTO(usuario);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UsuarioResponseDTO actualizarPerfil(Long idUsuario, UsuarioDTO dto) {
        Usuario usuario = buscarUsuarioOExcepcion(idUsuario);
        
        // Validar datos
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (usuarioDAO.existeEmail(dto.getEmail())) {
                throw new ValidationException("El email ya está en uso");
            }
        }
        
        // Actualizar
        UsuarioMapper.updateEntityFromDTO(usuario, dto);
        Usuario actualizado = usuarioDAO.actualizar(usuario);
        
        return UsuarioMapper.toResponseDTO(actualizado);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean cambiarPassword(Long idUsuario, CambioPasswordDTO dto) {
        Usuario usuario = buscarUsuarioOExcepcion(idUsuario);
        
        // Verificar contraseña actual
        if (!PasswordUtil.checkPassword(dto.getPasswordActual(), usuario.getPasswordHash())) {
            throw new ValidationException("La contraseña actual es incorrecta");
        }
        
        // Validar nueva contraseña
        if (!PasswordUtil.validarFortaleza(dto.getPasswordNueva())) {
            throw new ValidationException(PasswordUtil.obtenerMensajeRequisitos());
        }
        
        // Actualizar
        usuario.setPasswordHash(PasswordUtil.hashPassword(dto.getPasswordNueva()));
        usuarioDAO.actualizar(usuario);
        
        return true;
    }
    
    @Override
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioDAO.listarTodos().stream()
                .map(UsuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UsuarioResponseDTO> listarPorRol(RolUsuario rol) {
        return usuarioDAO.listarPorRol(rol).stream()
                .map(UsuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UsuarioResponseDTO cambiarRol(Long idUsuario, RolUsuario nuevoRol) {
        Usuario usuario = buscarUsuarioOExcepcion(idUsuario);
        usuario.setRol(nuevoRol);
        Usuario actualizado = usuarioDAO.actualizar(usuario);
        return UsuarioMapper.toResponseDTO(actualizado);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UsuarioResponseDTO cambiarEstado(Long idUsuario, boolean estado) {
        Usuario usuario = buscarUsuarioOExcepcion(idUsuario);
        usuario.setEstado(estado);
        Usuario actualizado = usuarioDAO.actualizar(usuario);
        return UsuarioMapper.toResponseDTO(actualizado);
    }
    
    @Override
    public boolean existeEmail(String email) {
        return usuarioDAO.existeEmail(email);
    }
    
    // Métodos auxiliares privados
    
    private void validarDatosRegistro(UsuarioRegistroDTO dto) {
        if (!ValidationUtil.isNotEmpty(dto.getNombre())) {
            throw new ValidationException("El nombre es obligatorio");
        }
        if (!ValidationUtil.isNotEmpty(dto.getApellido())) {
            throw new ValidationException("El apellido es obligatorio");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("El email no tiene un formato válido");
        }
        if (!PasswordUtil.validarFortaleza(dto.getPassword())) {
            throw new ValidationException(PasswordUtil.obtenerMensajeRequisitos());
        }
    }
    
    private Usuario buscarUsuarioOExcepcion(Long id) {
        return usuarioDAO.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Usuario", id));
    }
}