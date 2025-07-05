package vista.inventario;

import logica.Insumo;
import logica.RecetaDetalle;
import logica.Receta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import logica.ControladoraLogica;
import logica.Validacion;

public class RecetaForm extends JPanel {

    private JTextField txtNombreReceta;
    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarInsumo, btnGuardarReceta;
    private JComboBox<Insumo> comboInsumos;
    private JTextField txtCantidad, txtCosto;

    private ControladoraLogica controladoraLogica;

    public RecetaForm() {
        controladoraLogica = new ControladoraLogica();
        setLayout(new BorderLayout());
        setBackground(new Color(0x003366)); // azul oscuro predominante

        initComponents();
    }

    private void initComponents() {
        Color azul = new Color(81, 209, 246);
        Color negro = new Color(0x000000);
        Color blanco = new Color(0xFFFFFF);

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(azul);
        JLabel lblTitulo = new JLabel("Crear Nueva Receta");
        lblTitulo.setForeground(blanco);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelSuperior.add(lblTitulo);

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(azul);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre Receta:");
        lblNombre.setForeground(blanco);
        txtNombreReceta = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombreReceta, gbc);

        // Combo Insumo
        JLabel lblInsumo = new JLabel("Insumo:");
        lblInsumo.setForeground(blanco);

        
        // Aqui creo los insumos y esto hace que se actualicen 
        //      cuando se estan cambiando de panles
        comboInsumos = new JComboBox<>();
        recargarInsumos(); // este es el metodo q refresca los insumo
        
        comboInsumos.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                //aqui no se hace nada, pero esta definido
            }

            @Override
            public void focusLost(FocusEvent fe) {
                Insumo insumo = (Insumo) comboInsumos.getSelectedItem();
                txtCosto.setText(String.valueOf(insumo.getCostoInsumo()));
            }

        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblInsumo, gbc);
        gbc.gridx = 1;
        panelFormulario.add(comboInsumos, gbc);

        // Cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setForeground(blanco);
        txtCantidad = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblCantidad, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCantidad, gbc);

        // Costo
        JLabel lblCosto = new JLabel("Costo:");
        lblCosto.setForeground(blanco);
        txtCosto = new JTextField(10);
        txtCosto.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblCosto, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCosto, gbc);

        // Botón agregar insumo
        btnAgregarInsumo = new JButton("Agregar Insumo");
        btnAgregarInsumo.setBackground(negro);
        btnAgregarInsumo.setForeground(blanco);

        btnAgregarInsumo.addActionListener(e -> {
            txtNombreReceta.setEditable(false);
            Insumo insumo = (Insumo) comboInsumos.getSelectedItem();
            agregarInsumo(insumo);
        });

        gbc.gridx = 1;
        gbc.gridy = 4;
        panelFormulario.add(btnAgregarInsumo, gbc);

        // Tabla de insumos agregados
        modeloTabla = new DefaultTableModel(new Object[]{"Insumo", "Cantidad", "Costo"}, 0);
        tablaInsumos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInsumos);
        scrollTabla.setPreferredSize(new Dimension(600, 150));

        // Botón guardar 
        
        
        btnGuardarReceta = new JButton("Guardar Receta");
        btnGuardarReceta.setBackground(negro);
        btnGuardarReceta.setForeground(blanco);
        btnGuardarReceta.addActionListener(e -> guardarReceta());

        // Agregar todo
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(azul);
        panelInferior.add(scrollTabla, BorderLayout.CENTER);
        panelInferior.add(btnGuardarReceta, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void agregarInsumo(Insumo insumo) {

        if (!Validacion.esCostoValido(txtCantidad.getText())) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida. Debe ser un número positivo con hasta 2 decimales.");
            return;
        }

        BigDecimal cantidad = new BigDecimal(txtCantidad.getText());
        BigDecimal costoPorInsumo = new BigDecimal(txtCosto.getText());
        costoPorInsumo = costoPorInsumo.multiply(cantidad);
        modeloTabla.addRow(new Object[]{insumo.getNombreInsumo(), cantidad, costoPorInsumo});

        RecetaDetalle detalle = new RecetaDetalle();
        detalle.setIdInsumo(insumo);
        detalle.setCantidadInsumo(cantidad);
        detalle.setCostoInsumo(costoPorInsumo);

        // Limpiar campos
        txtCantidad.setText("");
        txtCosto.setText("");

    }

    private void guardarReceta() {
        String nombre = txtNombreReceta.getText();

        //Valida si el nombre no tiene caracteres especiales o numeros
        if (!Validacion.esNombreValido(nombre)) {
            JOptionPane.showMessageDialog(null, "Error: El nombre de la receta no puede contener números ni caracteres Especiales");
            txtNombreReceta.setEditable(true);
            return;
        }

        // Valida que el nombre no exista en la base de datos
        if (!controladoraLogica.validadNombreReceta(nombre)) {
            JOptionPane.showMessageDialog(null, "Error: El nombre de la receta ya existe.");
            txtNombreReceta.setEditable(true);
            return;
        }

        // Si pasa ambas validaciones, se guarda la receta y el detalle de la misma
        controladoraLogica.crearReceta(nombre);
        controladoraLogica.agregarDetalleReceta(modeloTabla, nombre);
        JOptionPane.showMessageDialog(this, "Receta guardada exitosamente");

        // Esto es para Vaciar Luego de Crear la receta y crear el detalle de la receta
        txtNombreReceta.setText("");
        for (int i = modeloTabla.getRowCount() - 1; i >= 0; i--) {
            modeloTabla.removeRow(i);
        }

    }

   public void recargarInsumos() {
    
       // se eliminan todos los Insumos para Cargarlos nuevamnete
       
    comboInsumos.removeAllItems(); // Limpia el combo actual
    List<Insumo> listaInsumos = controladoraLogica.listarInsumos(); // Carga actualizada
    for (Insumo insumo : listaInsumos) {
        comboInsumos.addItem(insumo);
    }
}

}
