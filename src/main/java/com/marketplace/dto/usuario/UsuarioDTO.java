package com.marketplace.dto.usuario;

import com.marketplace.enums.RolUsuario;
import java.time.LocalDateTime;

public class UsuarioDTO {
    
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private RolUsuario rol;
    private LocalDateTime fechaRegistro;
    private Boolean estado;
    
    public UsuarioDTO() {
    }
    
    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public RolUsuario getRol() {
        return rol;
    }
    
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}