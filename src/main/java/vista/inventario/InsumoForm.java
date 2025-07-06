package vista.inventario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import logica.ControladoraLogica;
import logica.Refrescar;

public class InsumoForm extends JPanel {

    private JTextField txtNombreInsumo;
    private JTextField txtUnidadMedida;
    private JTextField txtCostoInsumo;
    private JTextField txtStock;
    private JButton btnGuardar;
    private JButton btnCancelar;

    //controladora
    private ControladoraLogica controladoraLogica;
    private Refrescar refrescar;

    public InsumoForm(Refrescar refrescar) {
        this.refrescar = refrescar;
        controladoraLogica = new ControladoraLogica();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        // Paleta de colores
        Color azul = new Color(81, 209, 246);       // Azul principal
        Color negro = new Color(20, 20, 20);        // Negro profundo
        Color blanco = Color.WHITE;                // Blanco clásico

        // Fuentes
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(azul);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("Creaccion de Insumo");
        lblTitulo.setFont(titleFont);
        lblTitulo.setForeground(blanco);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(azul);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos y etiquetas
        JLabel lblNombre = new JLabel("Nombre del Insumo:");
        JLabel lblUnidad = new JLabel("Unidad de Medida:");
        JLabel lblCosto = new JLabel("Costo Inicial:");
        JLabel lblStock = new JLabel("Stock Inicial:");

        lblNombre.setForeground(negro);
        lblUnidad.setForeground(negro);
        lblCosto.setForeground(negro);
        lblStock.setForeground(negro);

        lblNombre.setFont(labelFont);
        lblUnidad.setFont(labelFont);
        lblCosto.setFont(labelFont);
        lblStock.setFont(labelFont);

        txtNombreInsumo = new JTextField(15);
        txtUnidadMedida = new JTextField(15);
        txtCostoInsumo = new JTextField(15);
        txtStock = new JTextField(15);

        txtNombreInsumo.setBackground(blanco);
        txtUnidadMedida.setBackground(blanco);
        txtCostoInsumo.setBackground(blanco);
        txtStock.setBackground(blanco);

        // Añadir componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblNombre, gbc);
        gbc.gridx = 1;
        formPanel.add(txtNombreInsumo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblUnidad, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUnidadMedida, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblCosto, gbc);
        gbc.gridx = 1;
        formPanel.add(txtCostoInsumo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblStock, gbc);
        gbc.gridx = 1;
        formPanel.add(txtStock, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(azul);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // Estilo de botones
        btnGuardar.setBackground(negro);
        btnGuardar.setForeground(blanco);
        btnCancelar.setBackground(negro);
        btnCancelar.setForeground(blanco);

        btnGuardar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        //Evento del boton guardar
        btnGuardar.addActionListener(e -> {

            String nombre = txtNombreInsumo.getText().trim();
            String unidad = txtUnidadMedida.getText().trim();
            String costoTexto = txtCostoInsumo.getText().trim();
            String stockTexto = txtStock.getText().trim();

            if (controladoraLogica.validarCampos(nombre, unidad, costoTexto, stockTexto)) {
                controladoraLogica.crearInsumo(nombre, unidad, costoTexto, stockTexto);

                JOptionPane.showMessageDialog(this, "✅ Todo los datos Validos, Se Guardo el insumo Correctamente");
                limpiarCampos(); // opcional
                // Recargar tabla
                if (refrescar != null) {
                    refrescar.refrescar();
                }
            }

        }
        );
    }
    // Getters para acceso externo

    public JTextField getTxtNombreInsumo() {
        return txtNombreInsumo;
    }

    public JTextField getTxtUnidadMedida() {
        return txtUnidadMedida;
    }

    public JTextField getTxtCostoInsumo() {
        return txtCostoInsumo;
    }

    public JTextField getTxtStock() {
        return txtStock;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    private void limpiarCampos() {
        txtNombreInsumo.setText("");
        txtUnidadMedida.setText("");
        txtCostoInsumo.setText("");
        txtStock.setText("");
    }

}
