/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import static java.awt.SystemColor.text;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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

    public boolean esNombrePermitido(String nombre) {
        Insumo insumo = controladoraPersistencia.esNombrePermitido(nombre);
        return (insumo == null); // Devuelve true si no existe (nombre permitido)
    }

    public boolean validadNombreReceta(String nombre) {
        boolean nombreValido = Validacion.esNombreValido(nombre);
        if (nombreValido) {
            Receta receta = controladoraPersistencia.esNombreRecetaPermitido(nombre);
            return (receta == null);
        }
        return false;

    }

    public List<Insumo> listarInsumos() {
        return controladoraPersistencia.listarInsumos();
    }

    public void crearReceta(String nombre) {
        Receta receta = new Receta();
        receta.setNombreReceta(nombre);
        receta.setCostoReceta(BigDecimal.ZERO);
        controladoraPersistencia.crearReceta(receta);
    }

    public void agregarDetalleReceta(DefaultTableModel modeloTabla, String nombreReceta) {
        if (!validarTablaDetalleRecetaVacia(modeloTabla)) {
            Receta receta = buscarReceta(nombreReceta);

            for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {

                Insumo insumo = buscarInsumoPorNombre(modeloTabla.getValueAt(fila, 0).toString());
                BigDecimal cantidad = new BigDecimal(modeloTabla.getValueAt(fila, 1).toString());

                controladoraPersistencia.insertarDetalleRecetaConSP(receta.getIdReceta(), insumo.getIdInsumo(), cantidad);
            }
        }
    }

    private boolean validarTablaDetalleRecetaVacia(DefaultTableModel modeloTabla) {
        return modeloTabla.getRowCount() == 0;
    }

    private Receta buscarReceta(String nombreReceta) {
        return controladoraPersistencia.buscarReceta(nombreReceta);
    }

    private Insumo buscarInsumoPorNombre(String nombreInsumo) {
        return controladoraPersistencia.buscarInsumo(nombreInsumo);
    }

    public void actualizarInsumo(int id, String nuevoNombre, double nuevoCosto) {
        Insumo insumoModificar = buscarInsumoPorId(id);
        insumoModificar.setCostoInsumo(BigDecimal.valueOf(nuevoCosto));
        insumoModificar.setNombreInsumo(nuevoNombre);
        controladoraPersistencia.modificarInsumo(insumoModificar);
    }

    public Insumo buscarInsumoPorId(int id) {
        return controladoraPersistencia.buscarInsumoPorId(id);
    }

    public void eliminarInsumoPorId(int id) {
        controladoraPersistencia.eliminarInsumoPorId(id);
    }

    public List<Receta> listarRecetas() {
        return controladoraPersistencia.listarRecetas();
    }

    public void eliminarRecetaPorId(int id) {
        controladoraPersistencia.eliminarRecetaPorId(id);
    }

    public List<RecetaDetalle> obtenerDetalleReceta(int idReceta) {
        List<RecetaDetalle> listaDeRecetaPorId=new ArrayList<>();
        List<RecetaDetalle> listaDeRecetas = controladoraPersistencia.obtenerDetalleReceta();
        for (RecetaDetalle detalles: listaDeRecetas ) {
            if (detalles.getIdReceta().getIdReceta().equals(idReceta)) {
                listaDeRecetaPorId.add(detalles);
            }
        }
        return listaDeRecetaPorId;

    }

}
