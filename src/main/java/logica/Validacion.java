/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.math.BigDecimal;
import persistencia.ControladoraPersistencia;

/**
 *
 * @author josepino
 */
public class Validacion {

    ControladoraPersistencia controladoraPersistencia = new ControladoraPersistencia();

    /**
     * Valida que el nombre del insumo: - No sea nulo ni vacío - Tenga una
     * longitud razonable - No contenga código SQL o HTML - Solo contenga
     * letras, espacios, tildes y guiones
     *
     * @param nombre Nombre a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean esNombreValido(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        // Limitar longitud
        if (nombre.length() > 100) {
            return false;
        }

        // Bloquear intentos obvios de SQLi o XSS
        String lower = nombre.toLowerCase();
        if (lower.contains("select") || lower.contains("insert") || lower.contains("delete")
                || lower.contains("update") || lower.contains("drop") || lower.contains("script")
                || lower.contains("<") || lower.contains(">") || lower.contains("'")
                || lower.contains("--") || lower.contains(";")) {
            return false;
        }

        // Solo permitir letras, espacios, tildes y guiones
        return nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ\\-\\s]+");
        
        
    }

    public static boolean esCostoValido(String costoTexto) {
        if (costoTexto == null || costoTexto.trim().isEmpty()) {
            return false;
        }

        try {
            BigDecimal costo = new BigDecimal(costoTexto);

            // No negativo
            if (costo.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }

            // (10,2) significa máximo 10 dígitos, con 2 decimales
            // -> parte entera: hasta 8 dígitos, decimales: hasta 2
            int scale = costo.scale();
            int precision = costo.precision();

            if (scale > 2) {
                return false;             // más de 2 decimales
            }
            if (precision - scale > 8) {
                return false; // parte entera mayor a 8 dígitos
            }
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esStockValido(String stockTexto) {
        if (stockTexto == null || stockTexto.trim().isEmpty()) {
            return false;
        }

        try {
            BigDecimal stock = new BigDecimal(stockTexto);

            // No negativo
            if (stock.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }

            // No mayor a 1000
            if (stock.compareTo(new BigDecimal("1000")) > 0) {
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

}
