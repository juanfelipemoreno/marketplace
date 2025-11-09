/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marketplace.dto.orden;

/**
 *
 * @author Felipe Moreno
 */
public class OrdenCreacionDTO {

    private String metodoPago;

    public OrdenCreacionDTO() {
    }

    public OrdenCreacionDTO(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
