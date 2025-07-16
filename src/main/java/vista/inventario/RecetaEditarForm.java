/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.inventario;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import logica.ControladoraLogica;
import logica.Insumo;
import logica.Receta;
import logica.RecetaDetalle;
import logica.Refrescar;
import logica.Validacion;
import vista.MainView;

/**
 *
 * @author josepino
 */
public class RecetaEditarForm extends JPanel {

    private boolean modoEdicion = false;
    private Receta recetaOriginal = null;

    private JTextField txtNombreReceta;
    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarInsumo, btnGuardarEdicionReceta, btnCancelar, btnEliminarInsumo;
    private JComboBox<Insumo> comboInsumos;
    private JTextField txtCantidad, txtCosto;

    private ControladoraLogica controladoraLogica;
    private Refrescar refrescar;

    private CardLayout cardLayout;
    private JPanel contenedor;
    private MainView mainView;

    private GestionRecetaForm gestionRecetaForm;

    public RecetaEditarForm(CardLayout cardLayout, JPanel contenedor, MainView mainView, Refrescar refrescar) {
        this.gestionRecetaForm = (GestionRecetaForm) refrescar;
        this.refrescar = refrescar;
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.mainView = mainView;
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
        JLabel lblTitulo = new JLabel("Edicion de Receta");
        lblTitulo.setForeground(blanco);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelSuperior.add(lblTitulo);

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(azul);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre Receta a Editar:");
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

        // Estilos
        btnAgregarInsumo = new JButton("Agregar Insumo");
        btnAgregarInsumo.setBackground(negro);
        btnAgregarInsumo.setForeground(blanco);

        btnEliminarInsumo = new JButton("Eliminar Insumo");
        btnEliminarInsumo.setBackground(new Color(153, 0, 0)); // Rojo oscuro para "eliminar"
        btnEliminarInsumo.setForeground(blanco);

// Acción
        btnAgregarInsumo.addActionListener(e -> {
            txtNombreReceta.setEditable(false);
            Insumo insumo = (Insumo) comboInsumos.getSelectedItem();
            agregarInsumo(insumo);
        });

// Panel para los botones (horizontal)
        JPanel panelBotonesInsumo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonesInsumo.setBackground(panelFormulario.getBackground()); // mismo fondo
        panelBotonesInsumo.add(btnAgregarInsumo);
        panelBotonesInsumo.add(btnEliminarInsumo);

// Posicionar en el formulario
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelFormulario.add(panelBotonesInsumo, gbc);

// Tabla de insumos agregados
        modeloTabla = new DefaultTableModel(new Object[]{"Insumo", "Cantidad", "Costo"}, 0);
        tablaInsumos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInsumos);
        scrollTabla.setPreferredSize(new Dimension(600, 150));

        // Botón guardar 
        btnGuardarEdicionReceta = new JButton("Guardar Edicion Receta");
        btnCancelar = new JButton("Cancelar");
        btnGuardarEdicionReceta.setBackground(negro);
        btnGuardarEdicionReceta.setForeground(blanco);
        btnCancelar.setBackground(negro);
        btnCancelar.setForeground(blanco);
        btnGuardarEdicionReceta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // aqui va la accion de Guardar la Edicion
            }
        });

        btnCancelar.addActionListener(e -> {
            cardLayout.show(contenedor, "gestionReceta");
            mainView.setTitle("Sistema de Gestión - Gestión de Recetas");
        });

        // Agregar todo
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(azul);

// 1. Tabla con scroll en el centro
        panelInferior.add(scrollTabla, BorderLayout.CENTER);

// 2. Panel de botones abajo (sur)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(azul);
        panelBotones.add(btnGuardarEdicionReceta);
        panelBotones.add(btnCancelar);

// 3. Agregar panel de botones al sur
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

// 4. Agregar panelInferior al panel principal
        add(panelInferior, BorderLayout.SOUTH);
    }
    private List<RecetaDetalle> detalles = new ArrayList<>();

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
        detalles.add(detalle);
        // Limpiar campos
        txtCantidad.setText("");
        txtCosto.setText("");

    }

 /*   private void guardarReceta() {
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
        Receta r = controladoraLogica.buscarReceta(nombre);
        try {
            Thread.sleep(300); // 300ms
        } catch (InterruptedException ex) {
            Logger.getLogger(RecetaForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        JOptionPane.showMessageDialog(this, "Receta guardada exitosamente");
        modeloTabla.setRowCount(0);
        // Esto es para Vaciar Luego de Crear la receta y crear el detalle de la receta
        txtNombreReceta.setText("");

        if (refrescar != null) {
            refrescar.refrescar();
        }

        cardLayout.show(contenedor, "inicio");

    }*/

    public void recargarInsumos() {

        // se eliminan todos los Insumos para Cargarlos nuevamnete
        comboInsumos.removeAllItems(); // Limpia el combo actual
        List<Insumo> listaInsumos = controladoraLogica.listarInsumos(); // Carga actualizada
        for (Insumo insumo : listaInsumos) {
            comboInsumos.addItem(insumo);
        }
    }

    public void actualizarComboInsumos(List<Insumo> insumosActualizados) {
        comboInsumos.removeAllItems(); // Limpia el combo actual
        for (Insumo insumo : insumosActualizados) {
            comboInsumos.addItem(insumo);
        }
    }

    public void cargarRecetaParaEditar(Receta receta, List<RecetaDetalle> detalles) {

        txtNombreReceta.setEditable(true);

        this.recetaOriginal = receta;
        this.modoEdicion = true;

        txtNombreReceta.setText(receta.getNombreReceta());

        // Si tienes otros campos como descripción, precio, etc., también los cargas aquí
        // txtDescripcion.setText(receta.getDescripcion());
        // Limpiar tabla de ingredientes actual
        modeloTabla.setRowCount(0);

        // Cargar los ingredientes desde detalles
        for (RecetaDetalle detalle : detalles) {
            Insumo insumo = detalle.getIdInsumo();
            modeloTabla.addRow(new Object[]{
                insumo.getNombreInsumo(),
                detalle.getCantidadInsumo(),
                detalle.getCostoInsumo()
            });
            System.out.println("nombre" + insumo.getNombreInsumo());
            System.out.println("cantidad" + detalle.getCantidadInsumo());
            System.out.println("costo" + detalle.getCostoInsumo());
        }

     
    }
    /*
    private void actualizarReceta() {
    String nuevoNombre = txtNombreReceta.getText().trim();
    
    List<Insumo> insumos = obtenerInsumosDesdeTabla();

    // Verifica si el nombre cambió
    if (!nuevoNombre.equalsIgnoreCase(recetaOriginal.getNombre())) {
        if (Repositorio.recetaYaExiste(nuevoNombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe una receta con ese nombre.");
            return;
        }
        recetaOriginal.setNombre(nuevoNombre);
    }

    recetaOriginal.setProcedimiento(procedimiento);
    recetaOriginal.setInsumos(insumos);

    Repositorio.actualizarReceta(recetaOriginal);
    JOptionPane.showMessageDialog(this, "Receta actualizada correctamente.");
    
    this.dispose(); // o volver a la pantalla anterior
}*/

}
