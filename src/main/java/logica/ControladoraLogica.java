/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import static java.awt.SystemColor.text;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import persistencia.ControladoraPersistencia;

/**
 *
 * @author josepino
 */
public class ControladoraLogica {

    ControladoraPersistencia controladoraPersistencia = new ControladoraPersistencia();

    public boolean validarCampos(String nombre, String unidad, String costoTexto, String stockTexto) {
        // Validaciones

        if (!Validacion.esNombreValido(nombre)) {
            JOptionPane.showMessageDialog(null, "Nombre inválido. Solo Texto por Favor.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!esNombrePermitido(nombre)) {
            JOptionPane.showMessageDialog(null, "Ya existe un Insumo con este nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validacion.esNombreValido(unidad)) {
            JOptionPane.showMessageDialog(null, "Nombre inválido. Solo Texto por Favor.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validacion.esCostoValido(costoTexto)) {
            JOptionPane.showMessageDialog(null, "Costo inválido. Debe ser un número positivo con hasta 2 decimales.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validacion.esStockValido(stockTexto)) {
            JOptionPane.showMessageDialog(null, "Stock inválido. Debe ser positivo y no exceder 1000.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void crearInsumo(String nombre, String unidad, String costoTexto, String stockTexto) {
        Insumo insumo = new Insumo();
        insumo.setNombreInsumo(nombre);
        insumo.setUnidadMedida(unidad);
        BigDecimal costoInsumo = new BigDecimal(costoTexto);
        insumo.setCostoInsumo(costoInsumo);
        BigDecimal stockInsumo = new BigDecimal(stockTexto);
        insumo.setStock(stockInsumo);
        controladoraPersistencia.crearInsumo(insumo);
    }

    private boolean esNombrePermitido(String nombre) {
        Insumo insumo = controladoraPersistencia.esNombrePermitido(nombre);
        return (insumo == null); // Devuelve true si no existe (nombre permitido)

    }

    public boolean validadNombreReceta(String nombre) {
        boolean nombreValido=Validacion.esNombreValido(nombre);        
        if(nombreValido){
        Receta receta=controladoraPersistencia.esNombreRecetaPermitido(nombre);
        return (receta==null);
        }
        return false;
    
        }

    public List<Insumo> listarInsumos() {
        return controladoraPersistencia.listarInsumos();
    }
    
    
    

}
