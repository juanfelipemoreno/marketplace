package com.marketplace.dto.carrito;


/**
 *
 * @author Felipe Moreno
 */
public class ItemCarritoDTO {

    private Long idProducto;
    private Integer cantidad;

    public ItemCarritoDTO() {
    }

    public ItemCarritoDTO(Long idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
