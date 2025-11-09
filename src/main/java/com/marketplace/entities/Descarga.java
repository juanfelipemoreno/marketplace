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

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "id_orden", nullable = false)
    private Long idOrden;

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

    public Long getIdDescarga() {
        return idDescarga;
    }

    public void setIdDescarga(Long idDescarga) {
        this.idDescarga = idDescarga;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
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
