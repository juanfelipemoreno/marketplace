package com.marketplace.dto.orden;

import java.time.LocalDateTime;

/**
 *
 * @author Felipe Moreno
 */
public class DescargaResponseDTO {

    private Long idProducto;
    private String nombreProducto;
    private String imagenProducto;
    private Long idOrden;
    private String numeroOrden;
    private LocalDateTime fechaCompra;
    private Integer numeroDescargas;
    private Integer limiteDescargas;
    private Boolean puedeDescargar;
    private String archivoUrl;
    private Long tamanoArchivo;

    public DescargaResponseDTO() {
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
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

    public Boolean getPuedeDescargar() {
        return puedeDescargar;
    }

    public void setPuedeDescargar(Boolean puedeDescargar) {
        this.puedeDescargar = puedeDescargar;
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
