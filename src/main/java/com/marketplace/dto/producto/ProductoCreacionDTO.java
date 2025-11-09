package com.marketplace.dto.producto;

import java.math.BigDecimal;

/**
 *
 * @author Felipe Moreno
 */
public class ProductoCreacionDTO {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String tipoProducto; // "FISICO" o "DIGITAL"
    private String imagenUrl;

    private Integer cantidadDisponible;

    private Integer licenciasTotales;
    private String archivoUrl;
    private Long tamanoArchivo;

    public ProductoCreacionDTO() {
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getLicenciasTotales() {
        return licenciasTotales;
    }

    public void setLicenciasTotales(Integer licenciasTotales) {
        this.licenciasTotales = licenciasTotales;
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
}
