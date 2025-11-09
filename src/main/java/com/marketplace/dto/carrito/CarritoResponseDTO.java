/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marketplace.dto.carrito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Moreno
 */
public class CarritoResponseDTO {

    private Long idCarrito;
    private List<ItemCarritoResponseDTO> items;
    private Integer totalItems;
    private BigDecimal totalPrecio;

    public CarritoResponseDTO() {
        this.items = new ArrayList<>();
        this.totalItems = 0;
        this.totalPrecio = BigDecimal.ZERO;
    }

    public Long getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Long idCarrito) {
        this.idCarrito = idCarrito;
    }

    public List<ItemCarritoResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCarritoResponseDTO> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalPrecio() {
        return totalPrecio;
    }

    public void setTotalPrecio(BigDecimal totalPrecio) {
        this.totalPrecio = totalPrecio;
    }
}
