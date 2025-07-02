/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logica.ControladoraLogica;
import logica.Insumo;

/**
 *
 * @author josepino
 */
public class GestionInsumoForm extends JPanel {

    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private ControladoraLogica controladora;
    private vista.inventario.InsumoForm insumoForm;

    private JPanel contentPane;
    private CardLayout cardLayout;

    public GestionInsumoForm() {
        controladora = new ControladoraLogica();
        setLayout(new BorderLayout());
        setBackground(new Color(0x003366)); // Azul oscuro
        initComponents();
        cargarInsumos();
    }

    private void initComponents() {
        // Título
        JLabel lblTitulo = new JLabel("Gestión de Insumos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"- ID -", "- Nombre -", "- Medida -", "- Stock -", "- Costo/Unitario -"}, 0);
        tablaInsumos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaInsumos);
        add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x003366));

        JButton btnCrear = new JButton("Crear Nuevo");
        JButton btnListar = new JButton("Litar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        btnCrear.addActionListener(e -> {
            cardLayout.show(contentPane, "insumo");
        });

        insumoForm = new vista.inventario.InsumoForm();
        contentPane.add(insumoForm, "insumos");

        btnListar.addActionListener(e -> cargarInsumos());

        btnEditar.addActionListener(e -> editarInsumo());

        btnEliminar.addActionListener(e -> eliminarInsumo());

        panelBotones.add(btnListar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarInsumos() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Insumo> insumos = controladora.listarInsumos();
        for (Insumo insumo : insumos) {
            modeloTabla.addRow(new Object[]{
                insumo.getIdInsumo(),
                insumo.getNombreInsumo(),
                insumo.getUnidadMedida(),
                insumo.getStock(),
                insumo.getCostoInsumo()
            });
        }
    }

    private void editarInsumo() {
        int fila = tablaInsumos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un insumo para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre del insumo:");
        String nuevaUnidad = JOptionPane.showInputDialog(this, "Nueva unidad:");
        String nuevoCostoStr = JOptionPane.showInputDialog(this, "Nuevo costo:");

        try {
            double nuevoCosto = Double.parseDouble(nuevoCostoStr);
            //controladora.editarInsumo(id, nuevoNombre, nuevaUnidad, nuevoCosto);
            JOptionPane.showMessageDialog(this, "Insumo actualizado.");
            cargarInsumos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Costo inválido.");
        }
    }

    private void eliminarInsumo() {
        int fila = tablaInsumos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un insumo para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar este insumo?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            //controladora.eliminarInsumo(id);
            JOptionPane.showMessageDialog(this, "Insumo eliminado.");
            cargarInsumos();
        }
    }
}
