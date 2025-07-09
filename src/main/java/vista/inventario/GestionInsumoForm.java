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
import logica.Refrescar;
import logica.Validacion;
import vista.MainView;

/**
 *
 * @author josepino
 */
public class GestionInsumoForm extends JPanel  implements Refrescar{

    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private ControladoraLogica controladora;
    private vista.inventario.InsumoForm insumoForm;

    private JPanel contentPane;
    private CardLayout cardLayout;
    private MainView mainView;

    public GestionInsumoForm(MainView mainView) {
        this.mainView = mainView;
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
        modeloTabla = new DefaultTableModel(new Object[]{"- ID -", "- Nombre -", "- Medida -", "- Stock -", "- Costo/Unitario -"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaInsumos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaInsumos);
        add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x003366));

        JButton btnCrear = new JButton("Crear Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        insumoForm = new vista.inventario.InsumoForm(this, cardLayout, contentPane);
        contentPane.add(insumoForm, "insumos");

        btnCrear.addActionListener(e -> {
            mainView.mostrarFormularioInsumo(this);
            cardLayout.show(contentPane, "insumo");
        });

       

        btnEditar.addActionListener(e -> editarInsumo());

        btnEliminar.addActionListener(e -> eliminarInsumo());

        panelBotones.add(btnCrear);
       
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarInsumos() {
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
        String nombreActual = (String) modeloTabla.getValueAt(fila, 1);
        String costoActual = (String) modeloTabla.getValueAt(fila, 4).toString();

        JTextField campoNombre = new JTextField(nombreActual);
        JTextField campoCosto = new JTextField(costoActual);

        Object[] campos = {
            "Nuevo Nombre:", campoNombre,
            "Nuevo Costo Unitario:", campoCosto
        };
        int opcion = JOptionPane.showConfirmDialog(this, campos, "Editar Insumo", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoNombre = campoNombre.getText().trim();
            String nuevoCostoStr = campoCosto.getText().trim();

            if (!nuevoNombre.equals(nombreActual)) {
                // Solo si el nombre cambió, validar si está permitido y es válido
                if (!controladora.esNombrePermitido(nuevoNombre)) {
                    JOptionPane.showMessageDialog(null, "Ya existe un Insumo con este nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!Validacion.esNombreValido(nuevoNombre)) {
                    JOptionPane.showMessageDialog(null, "Algo en el nombre es inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!Validacion.esCostoValido(nuevoCostoStr)) {
                    JOptionPane.showMessageDialog(null, "Costo Incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
            try {
                double nuevoCosto = Double.parseDouble(nuevoCostoStr);
                controladora.actualizarInsumo(id, nuevoNombre, nuevoCosto);
                JOptionPane.showMessageDialog(this,
                        "El insumo fue actualizado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarInsumos(); //
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Costo inválido.");
            }
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
            controladora.eliminarInsumoPorId(id);
            JOptionPane.showMessageDialog(this, "Insumo eliminado completamente.");
            cargarInsumos();
        }
    }

    @Override
    public void refrescar() {
       cargarInsumos();
    }
}
