package com.marketplace.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utilidad para manejo de fechas
 * @author Felipe Moreno
 */
public class DateUtil {
    
    // Formateadores comunes
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Formatea una fecha a formato dd/MM/yyyy
     * @param date Fecha a formatear
     * @return Fecha formateada
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Formatea una fecha y hora a formato dd/MM/yyyy HH:mm:ss
     * @param dateTime Fecha y hora a formatear
     * @return Fecha y hora formateada
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Formatea una fecha y hora a formato ISO
     * @param dateTime Fecha y hora a formatear
     * @return Fecha y hora en formato ISO
     */
    public static String formatToISO(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(ISO_FORMATTER);
    }
    
    /**
     * Convierte una cadena a LocalDate
     * @param dateString Cadena con formato dd/MM/yyyy
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * Convierte una cadena a LocalDateTime
     * @param dateTimeString Cadena con formato dd/MM/yyyy HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) return null;
        return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    }
    
    /**
     * Obtiene la fecha y hora actual
     * @return LocalDateTime actual
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
    
    /**
     * Obtiene la fecha actual
     * @return LocalDate actual
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
    
    /**
     * Obtiene el inicio del día de una fecha
     * @param date Fecha
     * @return LocalDateTime al inicio del día (00:00:00)
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay();
    }
    
    /**
     * Obtiene el final del día de una fecha
     * @param date Fecha
     * @return LocalDateTime al final del día (23:59:59)
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atTime(23, 59, 59);
    }
    
    /**
     * Calcula la diferencia en días entre dos fechas
     * @param start Fecha inicial
     * @param end Fecha final
     * @return Número de días de diferencia
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * Verifica si una fecha es anterior a otra
     * @param date1 Primera fecha
     * @param date2 Segunda fecha
     * @return true si date1 es anterior a date2
     */
    public static boolean isBefore(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) return false;
        return date1.isBefore(date2);
    }
    
    /**
     * Verifica si una fecha es posterior a otra
     * @param date1 Primera fecha
     * @param date2 Segunda fecha
     * @return true si date1 es posterior a date2
     */
    public static boolean isAfter(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) return false;
        return date1.isAfter(date2);
    }
    
    /**
     * Obtiene el primer día del mes actual
     * @return LocalDateTime del primer día del mes a las 00:00:00
     */
    public static LocalDateTime startOfMonth() {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay();
    }
    
    /**
     * Obtiene el último día del mes actual
     * @return LocalDateTime del último día del mes a las 23:59:59
     */
    public static LocalDateTime endOfMonth() {
        LocalDate lastDay = LocalDate.now().withDayOfMonth(
            LocalDate.now().lengthOfMonth()
        );
        return lastDay.atTime(23, 59, 59);
    }
}