package vista.inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import logica.Receta;
import logica.ControladoraLogica;
import logica.Insumo;
import logica.RecetaDetalle;
import logica.Refrescar;
import vista.MainView;

public class GestionRecetaForm extends JPanel implements Refrescar {

    // === Atributos ===
    private JTable tablaRecetas;
    private DefaultTableModel modeloTabla;

    private JTable tablaIngredientes;
    private DefaultTableModel modeloIngredientes;

    private JLabel lblNombre, lblPrecio, lblIngredientes;
    private JPanel panelDetalle;

    private JPanel contentPane;
    private CardLayout cardLayout;

    private RecetaForm recetaForm;
    private RecetaEditarForm recetaEditarForm;

    private ControladoraLogica controladoraLogica;
    private MainView mainView;

    // === Constructor ===
    public GestionRecetaForm(MainView mainView) {
        this.mainView = mainView;
        this.controladoraLogica = new ControladoraLogica();

        setLayout(new BorderLayout());
        setBackground(new Color(0x003366)); // Azul oscuro

        initComponents();
        cargarRecetas();
    }

    // === Inicialización de Componentes ===
    private void initComponents() {
        // Título superior
        JLabel lblTitulo = new JLabel("Gestión de Recetas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Panel derecho: detalles de receta
        panelDetalle = new JPanel();
        panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));
        panelDetalle.setBackground(new Color(0xE6F0FA)); // Azul claro

        lblNombre = new JLabel("Nombre: ");
        lblPrecio = new JLabel("Precio: ");
        lblIngredientes = new JLabel("Ingredientes: ");

        modeloIngredientes = new DefaultTableModel(new Object[]{"Insumo", "Cantidad", "Costo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaIngredientes = new JTable(modeloIngredientes);
        tablaIngredientes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaIngredientes.setRowHeight(22);
        tablaIngredientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        panelDetalle.add(new JScrollPane(tablaIngredientes));
        panelDetalle.add(lblNombre);
        panelDetalle.add(lblPrecio);
        panelDetalle.add(lblIngredientes);

        // Panel izquierdo: lista de recetas
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Receta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRecetas = new JTable(modeloTabla);
        tablaRecetas.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                mostrarDetalleRecetaSeleccionada();
            }
        });

        // División entre tabla y detalle
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(550); // ancho para la tabla
        splitPane.setLeftComponent(new JScrollPane(tablaRecetas));
        splitPane.setRightComponent(panelDetalle);
        add(splitPane, BorderLayout.CENTER);

        // Panel de botones inferiores
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x003366));

        JButton btnCrear = new JButton("Crear Nueva");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // Panel de navegación con CardLayout
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        // Eventos de botones
        btnCrear.addActionListener(e -> {
            refrescar();
            recetaForm = new RecetaForm(cardLayout, contentPane, mainView, this);
            contentPane.add(recetaForm, "gestionReceta");
            mainView.mostrarFormularioReceta();
            cardLayout.show(contentPane, "gestionReceta");
        });

        btnEditar.addActionListener(e -> editarReceta());
        btnEliminar.addActionListener(e -> eliminarReceta());
    }

    // === Cargar recetas desde lógica ===
    public void cargarRecetas() {
        modeloTabla.setRowCount(0);
        List<Receta> recetas = controladoraLogica.listarRecetas();

        for (Receta receta : recetas) {
            modeloTabla.addRow(new Object[]{
                receta.getIdReceta(),
                receta.getNombreReceta(),
                receta.getCostoReceta()
            });
            System.out.println("id de la receta " + receta.getIdReceta());
            System.out.println("nombre de la receta " + receta.getNombreReceta());
            System.out.println("valor de la receta " + receta.getCostoReceta());
        }
    }

    // === Mostrar detalle de receta seleccionada ===
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
        modeloIngredientes.setRowCount(0);

        for (RecetaDetalle ing : ingredientes) {
            modeloIngredientes.addRow(new Object[]{
                ing.getIdInsumo().getNombreInsumo(),
                ing.getCantidadInsumo().toPlainString(),
                "$" + ing.getCostoInsumo().toPlainString()
            });
        }

        lblIngredientes.setText("Ingredientes: " + ingredientes.size());
        System.out.println("Cargando detalle de receta: " + nombre + " con " + ingredientes.size() + " ingredientes.");
        panelDetalle.revalidate();
        panelDetalle.repaint();
    }

    // === Editar receta seleccionada ===
    private void editarReceta() {
        int fila = tablaRecetas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una receta para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        Receta recetaSeleccionada = controladoraLogica.buscarRecetaPorId(id);
        List<RecetaDetalle> detalles = controladoraLogica.obtenerDetalleReceta(id);

        int opcion = JOptionPane.showConfirmDialog(this,
                "Desea Editar esta Receta?",
                "Editar Receta",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            recetaEditarForm = new RecetaEditarForm(cardLayout, contentPane, mainView, this); // actualiza el panel con dependencias
            contentPane.add(recetaEditarForm, "editarReceta"); // asegúrate de usar una key válida
            recetaEditarForm.cargarRecetaParaEditar(recetaSeleccionada, detalles);
            mainView.mostrarFormularioReceta();
            cardLayout.show(contentPane, "editarReceta");
            mainView.setTitle("Sistema de Gestión - Editar Receta");

        }
    }

    // === Eliminar receta seleccionada ===
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

    // === Refrescar combo de insumos ===
    @Override
    public void refrescar() {
        List<Insumo> insumosActualizados = recargarInsumos();
        recetaForm.actualizarComboInsumos(insumosActualizados);
    }

    // === Obtener insumos desde la lógica ===
    private List<Insumo> recargarInsumos() {
        return controladoraLogica.listarInsumos();
    }
}
