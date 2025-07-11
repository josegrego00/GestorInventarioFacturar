package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.Refrescar;
import vista.inventario.GestionInsumoForm;
import vista.inventario.GestionRecetaForm;

public class MainView extends JFrame {

    private JPanel contentPane;
    private CardLayout cardLayout;

    // Paneles para cada módulo
    private vista.inventario.InsumoForm insumoForm;
    private vista.inventario.RecetaForm recetaForm;
    private vista.inventario.GestionInsumoForm gestionInsumoForm;
    private vista.inventario.GestionRecetaForm gestionRecetaForm;

    public MainView() {

        gestionRecetaForm = new vista.inventario.GestionRecetaForm(this);

        setTitle("Sistema de Gestión - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {

        // Configuración de colores
        Color azulOscuro = new Color(0, 51, 102);
        Color azulClaro = new Color(173, 216, 230);
        Color blanco = Color.WHITE;

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barra de menú superior
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(azulOscuro);
        menuBar.setForeground(blanco);

        // Menú Inventario
        JMenu menuInventario = new JMenu("Inventario");
        menuInventario.setForeground(blanco);
        JMenuItem itemInsumos = new JMenuItem("Gestión de Insumos");
        itemInsumos.addActionListener(e -> {
            cardLayout.show(contentPane, "gestionInsumo");
            setTitle("Sistema de Gestión - Gesstion de Insumos");

        });
        menuInventario.add(itemInsumos);

        // Menú Ventas
        JMenu menuVentas = new JMenu("Ventas");
        menuVentas.setForeground(blanco);

        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setForeground(blanco);

        // Agregar menús a la barra
        menuBar.add(menuInventario);
        menuBar.add(menuVentas);
        menuBar.add(menuReportes);

        // Panel lateral (sidebar)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(azulOscuro);
        sidebar.setPreferredSize(new Dimension(100, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Botones del sidebar
        JButton btnInsumos = crearBotonSidebar("Insumos", "src/resources/insumos.png");
        btnInsumos.addActionListener(e -> {
            cardLayout.show(contentPane, "gestionInsumo");

            setTitle("Sistema de Gestión - Gesstion de Insumos");
        });

        // Botones del sidebar
        JButton btnRecetas = crearBotonSidebar("Recetas", "src/resources/recetas.png");
        btnRecetas.addActionListener(e -> {
            cardLayout.show(contentPane, "gestionReceta");
            setTitle("Sistema de Gestión - Módulo de Recetas");
            System.out.println("precione aqui");
        });

        sidebar.add(btnRecetas);

        JButton btnVentas = crearBotonSidebar("Ventas", "src/resources/ventas.png");
        JButton btnCompras = crearBotonSidebar("Compras", "src/resources/compras.png");
        JButton btnReportes = crearBotonSidebar("Reportes", "src/resources/reportes.png");

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnInsumos);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnVentas);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnCompras);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnReportes);

        // Panel de contenido (CardLayout para cambiar entre módulos)
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        contentPane.setBackground(blanco);

        // Inicializar módulos
        // aqui es donde Cargo los Modulos al Menu Principal
        gestionInsumoForm = new vista.inventario.GestionInsumoForm(this);
        contentPane.add(gestionInsumoForm, "gestionInsumo");

        gestionRecetaForm = new vista.inventario.GestionRecetaForm(this);
        contentPane.add(gestionRecetaForm, "gestionReceta");

        // Agregar Imdumo al contentPane
        contentPane.add(new JPanel(), "inicio"); // Panel vacío inicial

        // Agregar componentes al mainPanel
        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPane, BorderLayout.CENTER);

        // Panel de estado inferior
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(azulClaro);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Sistema de Gestión v1.0 | Usuario: Admin");
        statusPanel.add(statusLabel);

        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        cardLayout.show(contentPane, "inicio");

        add(mainPanel);

    }

    private JButton crearBotonSidebar(String texto, String iconPath) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 50));
        boton.setBackground(new Color(0, 51, 102));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + iconPath);
        }

        return boton;
    }

    public void mostrarModuloInsumos() {
        cardLayout.show(contentPane, "insumos");
        setTitle("Sistema de Gestión - Módulo de Insumos");
    }

    public void mostrarModuloRecetaForm() {
        cardLayout.show(contentPane, "receta");
        setTitle("Sistema de Gestión - Crear o Editar Receta");
    }

    public void mostrarFormularioInsumo(Refrescar refrescar) {
        vista.inventario.InsumoForm insumoForm = new vista.inventario.InsumoForm(refrescar, cardLayout, contentPane);
        contentPane.add(insumoForm, "insumoForm");
        cardLayout.show(contentPane, "insumoForm");
        setTitle("Sistema de Gestión - Crear Nuevo Insumo");
    }

    public void mostrarFormularioReceta() {
        vista.inventario.RecetaForm recetaForm = new vista.inventario.RecetaForm(cardLayout, contentPane, this, gestionRecetaForm);
        contentPane.add(recetaForm, "gestionRecetaC");
        cardLayout.show(contentPane, "gestionRecetaC");
        recetaForm.recargarInsumos();

        setTitle("Sistema de Gestión - Crear Nuevo Receta");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
