package com.marketplace.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Felipe Moreno
 */
@Entity
@Table(name = "inventario_digital")
public class InventarioDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario_digital")
    private Long idInventarioDigital;

    @OneToOne
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Producto producto;

    @Column(name = "licencias_totales")
    private Integer licenciasTotales;

    @Column(name = "licencias_disponibles")
    private Integer licenciasDisponibles;

    @Column(name = "archivo_url", length = 500)
    private String archivoUrl;

    @Column(name = "tamano_archivo")
    private Long tamanoArchivo;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public InventarioDigital() {
        this.licenciasTotales = 0;
        this.licenciasDisponibles = 0;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdInventarioDigital() {
        return idInventarioDigital;
    }

    public void setIdInventarioDigital(Long idInventarioDigital) {
        this.idInventarioDigital = idInventarioDigital;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getLicenciasTotales() {
        return licenciasTotales;
    }

    public void setLicenciasTotales(Integer licenciasTotales) {
        this.licenciasTotales = licenciasTotales;
    }

    public Integer getLicenciasDisponibles() {
        return licenciasDisponibles;
    }

    public void setLicenciasDisponibles(Integer licenciasDisponibles) {
        this.licenciasDisponibles = licenciasDisponibles;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public Long getTamanoArchivo() {
        return tamanoArchivo;
    }

    public void setTamanoArchivo(Long tamanoArchivo) {
        this.tamanoArchivo = tamanoArchivo;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
