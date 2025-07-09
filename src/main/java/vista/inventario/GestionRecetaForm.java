package vista.inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import logica.Receta;
import logica.ControladoraLogica;
import logica.Insumo;
import logica.RecetaDetalle;
import logica.Refrescar;
import vista.MainView;

public class GestionRecetaForm extends JPanel implements Refrescar {

    private JTable tablaRecetas;
    private DefaultTableModel modeloTabla;
    private ControladoraLogica controladoraLogica;

    private RecetaForm recetaForm;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private MainView mainView;

    private JPanel panelDetalle;
    private JTable tablaIngredientes;
    private DefaultTableModel modeloIngredientes;
    private JLabel lblNombre, lblPrecio, lblIngredientes;

    public GestionRecetaForm(MainView mainView) {
        this.mainView = mainView;
        controladoraLogica = new ControladoraLogica();
        setLayout(new BorderLayout());
        setBackground(new Color(0x003366)); // Azul oscuro
        initComponents();
        cargarRecetas();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Gestión de Recetas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // aqui es donde van los detalles de las recetas
        panelDetalle = new JPanel();
        panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));
        panelDetalle.setBackground(new Color(0xE6F0FA)); // Azul claro

        modeloIngredientes = new DefaultTableModel(new Object[]{"Insumo", "Cantidad", "Costo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lblNombre = new JLabel("Nombre: ");
        lblPrecio = new JLabel("Precio: ");
        lblIngredientes = new JLabel("Ingredientes: ");
        tablaIngredientes = new JTable(modeloIngredientes);
        tablaIngredientes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaIngredientes.setRowHeight(22);
        tablaIngredientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        panelDetalle.add(new JScrollPane(tablaIngredientes));

        panelDetalle.add(lblNombre);
        panelDetalle.add(lblPrecio);
        panelDetalle.add(lblIngredientes);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Receta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRecetas = new JTable(modeloTabla);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(150);

        splitPane.setDividerLocation(550); // ancho para la tabla
        splitPane.setLeftComponent(new JScrollPane(tablaRecetas));
        splitPane.setRightComponent(panelDetalle);

        add(splitPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x003366));

        JButton btnCrear = new JButton("Crear Nueva");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        recetaForm = new vista.inventario.RecetaForm(cardLayout, contentPane, mainView );
        contentPane.add(recetaForm, "gestionReceta");

        btnCrear.addActionListener(e -> {
            refrescar();
            mainView.mostrarFormularioReceta(); // Cambiamos de panel desde la vista principal
            cardLayout.show(contentPane, "gestionReceta");
        });

        tablaRecetas.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                mostrarDetalleRecetaSeleccionada();
            }
        });

        btnEditar.addActionListener(e -> editarReceta());
        btnEliminar.addActionListener(e -> eliminarReceta());

        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarRecetas() {
        modeloTabla.setRowCount(0);
        List<Receta> recetas = controladoraLogica.listarRecetas(); // Asegúrate de tener este método
        for (Receta receta : recetas) {
            modeloTabla.addRow(new Object[]{
                receta.getIdReceta(),
                receta.getNombreReceta(),
                receta.getCostoReceta()
            });
        }
    }

    private void crearReceta() {
        // Puedes crear un formulario similar a InsumoForm
        JOptionPane.showMessageDialog(this, "Abrir formulario para crear receta.");
    }

    private void editarReceta() {
        int fila = tablaRecetas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una receta para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombreActual = (String) modeloTabla.getValueAt(fila, 1);
        String descripcionActual = (String) modeloTabla.getValueAt(fila, 2);

        JTextField campoNombre = new JTextField(nombreActual);
        JTextField campoDescripcion = new JTextField(descripcionActual);

        Object[] campos = {
            "Nuevo Nombre:", campoNombre,
            "Nueva Descripción:", campoDescripcion
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Editar Receta", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoNombre = campoNombre.getText().trim();
            String nuevaDescripcion = campoDescripcion.getText().trim();

            // Puedes validar aquí si lo deseas
            //controladora.actualizarReceta(id, nuevoNombre, nuevaDescripcion);
            JOptionPane.showMessageDialog(this, "Receta actualizada exitosamente.");
            cargarRecetas();
        }
    }

    private void eliminarReceta() {
        int fila = tablaRecetas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una receta para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar esta receta?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controladoraLogica.eliminarRecetaPorId(id);
            JOptionPane.showMessageDialog(this, "Receta eliminada correctamente.");
            cargarRecetas();
        }
    }

    private void mostrarDetalleRecetaSeleccionada() {
        int fila = tablaRecetas.getSelectedRow();
        if (fila == -1) {
            return;
        }

        int idReceta = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        BigDecimal precio = (BigDecimal) modeloTabla.getValueAt(fila, 2);

        lblNombre.setText("Nombre Receta: " + nombre);
        lblPrecio.setText("Precio Receta: $" + precio);

        List<RecetaDetalle> ingredientes = controladoraLogica.obtenerDetalleReceta(idReceta);

        modeloIngredientes.setRowCount(0); // Limpia tabla antes de cargar nuevos datos

        for (RecetaDetalle ing : ingredientes) {
            String nombreInsumo = ing.getIdInsumo().getNombreInsumo();
            String cantidad = ing.getCantidadInsumo().toPlainString();
            String costo = "$" + ing.getCostoInsumo().toPlainString();

            modeloIngredientes.addRow(new Object[]{nombreInsumo, cantidad, costo});
        }
        lblIngredientes.setText("Ingredientes: " + ingredientes.size());

    }

    @Override
    public void refrescar() {
        List<Insumo> insumosActualizados = recargarInsumos();
        recetaForm.actualizarComboInsumos(insumosActualizados); 
    }

    private List<Insumo> recargarInsumos() {
        return controladoraLogica.listarInsumos();
    }

}
