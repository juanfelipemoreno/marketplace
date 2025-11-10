package com.marketplace.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Felipe Moreno
 */
@Entity
@Table(name = "descargas")
public class Descarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descarga")
    private Long idDescarga;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private Orden orden;

    @Column(name = "fecha_descarga")
    private LocalDateTime fechaDescarga;

    @Column(name = "numero_descargas", nullable = false)
    private Integer numeroDescargas;

    @Column(name = "limite_descargas", nullable = false)
    private Integer limiteDescargas;

    public Descarga() {
        this.fechaDescarga = LocalDateTime.now();
        this.numeroDescargas = 0;
        this.limiteDescargas = 3;
    }

    // Getters y Setters
    public Long getIdDescarga() {
        return idDescarga;
    }

    public void setIdDescarga(Long idDescarga) {
        this.idDescarga = idDescarga;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public LocalDateTime getFechaDescarga() {
        return fechaDescarga;
    }

    public void setFechaDescarga(LocalDateTime fechaDescarga) {
        this.fechaDescarga = fechaDescarga;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Integer getLimiteDescargas() {
        return limiteDescargas;
    }

    public void setLimiteDescargas(Integer limiteDescargas) {
        this.limiteDescargas = limiteDescargas;
    }
}
