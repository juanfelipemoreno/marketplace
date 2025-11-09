/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marketplace.enums;

/**
 *
 * @author Felipe Moreno
 */
public enum EstadoOrden {
    PENDIENTE,      // Orden creada, esperando confirmación
    CONFIRMADA,     // Orden confirmada y procesada
    CANCELADA,      // Orden cancelada por el usuario o sistema
    COMPLETADA      // Orden completada (productos entregados/descargados)
}
