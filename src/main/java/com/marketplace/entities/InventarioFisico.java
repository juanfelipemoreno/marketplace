/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marketplace.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Felipe Moreno
 */
@Entity
@Table(name = "inventario_fisico")
public class InventarioFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario_fisico")
    private Long idInventarioFisico;

    @OneToOne
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Producto producto;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @Column(name = "cantidad_reservada", nullable = false)
    private Integer cantidadReservada;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public InventarioFisico() {
        this.cantidadDisponible = 0;
        this.cantidadReservada = 0;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdInventarioFisico() {
        return idInventarioFisico;
    }

    public void setIdInventarioFisico(Long idInventarioFisico) {
        this.idInventarioFisico = idInventarioFisico;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
