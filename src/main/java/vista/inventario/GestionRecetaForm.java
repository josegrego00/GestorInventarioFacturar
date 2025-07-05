package vista.inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import logica.Receta;
import logica.ControladoraLogica;
import vista.MainView;

public class GestionRecetaForm extends JPanel {

    private JTable tablaRecetas;
    private DefaultTableModel modeloTabla;
    private ControladoraLogica controladora;

    private RecetaForm recetaForm;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private MainView mainView;

    public GestionRecetaForm(MainView mainView) {
        this.mainView = mainView;
        controladora = new ControladoraLogica();
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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Receta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRecetas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaRecetas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x003366));

        JButton btnCrear = new JButton("Crear Nueva");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        recetaForm = new vista.inventario.RecetaForm();
        contentPane.add(recetaForm, "gestionReceta");

        btnCrear.addActionListener(e -> {
            recetaForm.recargarInsumos(); // si hace falta
            mainView.mostrarModuloRecetaForm(); // Cambiamos de panel desde la vista principal
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
        List<Receta> recetas = controladora.listarRecetas(); // Asegúrate de tener este método
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
            controladora.eliminarRecetaPorId(id);
            JOptionPane.showMessageDialog(this, "Receta eliminada correctamente.");
            cargarRecetas();
        }
    }
}
