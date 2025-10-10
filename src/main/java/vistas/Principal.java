package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Principal extends JFrame {
    
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rolUsuario;
    
    private JLabel lblTotalIngresos;
    private JLabel lblTotalGastos;
    private JLabel lblBalance;
    private JTable tablaTransacciones;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtFecha;
    private JTextField txtReferencia;
    private JTextField txtTipo;
    private JTextField txtCategoria;
    private JTextField txtMonto;
    private JTextArea txtDescripcion;
    private JButton btnAgregarPartida;
    
    public Principal(String nombre, String apellido, String rol) {
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.rolUsuario = rol;
        
        inicializarComponentes();
        cargarDatos();
        configurarPermisos();
    }
    
    private void configurarPermisos() {
        // Configurar permisos según el rol
        if (rolUsuario.equalsIgnoreCase("Usuario")) {
            // Usuario normal: solo puede agregar partidas
            btnAgregarPartida.setEnabled(true);
        } else if (rolUsuario.equalsIgnoreCase("Admin") || rolUsuario.equalsIgnoreCase("Administrador")) {
            // Admin: acceso completo
            btnAgregarPartida.setEnabled(true);
        } else if (rolUsuario.equalsIgnoreCase("Contador")) {
            // Contador: puede agregar partidas y ver reportes
            btnAgregarPartida.setEnabled(true);
        } else {
            // Rol no reconocido: solo lectura
            btnAgregarPartida.setEnabled(false);
        }
    }
    
    private void inicializarComponentes() {
        setTitle("ContaBook - Libro de Cuentas");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(240, 242, 235));
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== HEADER ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(240, 242, 235));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("ContaBook - Libro de Cuentas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.BLACK);
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setBackground(new Color(240, 242, 235));
        
        JLabel lblUsuario = new JLabel(nombreUsuario + " " + apellidoUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblRol = new JLabel(rolUsuario);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(100, 100, 100));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(140, 160, 140));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setBackground(new Color(240, 242, 235));
        panelInfoUsuario.add(lblUsuario);
        panelInfoUsuario.add(lblRol);
        
        panelUsuario.add(panelInfoUsuario);
        panelUsuario.add(Box.createRigidArea(new Dimension(15, 0)));
        panelUsuario.add(btnCerrarSesion);
        
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);
        
        // ========== TABS ==========
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTabs.setBackground(new Color(240, 242, 235));
        
        JButton btnPanelPrincipal = crearBotonTab("Panel Principal", true);
        JButton btnPeriodos = crearBotonTab("Períodos", false);
        JButton btnReportes = crearBotonTab("Reportes", false);
        JButton btnUsuarios = crearBotonTab("Usuarios", false);
        
        // Deshabilitar según el rol
        if (!rolUsuario.equalsIgnoreCase("Admin")) {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setBackground(new Color(180, 180, 180));
        }
        
        panelTabs.add(btnPanelPrincipal);
        panelTabs.add(btnPeriodos);
        panelTabs.add(btnReportes);
        panelTabs.add(btnUsuarios);
        
        // ========== CONTENIDO PRINCIPAL ==========
        JPanel panelContenido = new JPanel(new BorderLayout(15, 15));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel Izquierdo - Formulario
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(350, 0));
        
        // Resumen Financiero
        JPanel panelResumen = new JPanel(new GridLayout(3, 1, 10, 10));
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        panelResumen.add(crearPanelResumen("Total Ingresos", "0 transacciones", "green"));
        panelResumen.add(crearPanelResumen("Total Gastos", "0 transacciones", "red"));
        panelResumen.add(crearPanelResumen("Balance", "Comienzo del período", "blue"));
        
        // Formulario
        JLabel lblFormTitulo = new JLabel("+ Nueva Partida Contable");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        
        txtFecha = crearCampoFormulario(panelFormulario, "Fecha");
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        
        txtReferencia = crearCampoFormulario(panelFormulario, "Referencia");
        txtTipo = crearCampoFormulario(panelFormulario, "Tipo");
        txtCategoria = crearCampoFormulario(panelFormulario, "Categoría");
        
        JLabel lblDesc = new JLabel("Descripción");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelFormulario.add(lblDesc);
        
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panelFormulario.add(scrollDesc);
        
        txtMonto = crearCampoFormulario(panelFormulario, "Monto ($)");
        
        btnAgregarPartida = new JButton("+ Agregar Partida");
        btnAgregarPartida.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregarPartida.setBackground(new Color(140, 160, 140));
        btnAgregarPartida.setForeground(Color.WHITE);
        btnAgregarPartida.setFocusPainted(false);
        btnAgregarPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregarPartida.setMaximumSize(new Dimension(300, 40));
        btnAgregarPartida.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarPartida.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnAgregarPartida.addActionListener(e -> agregarPartida());
        
        panelIzquierdo.add(panelResumen);
        panelIzquierdo.add(lblFormTitulo);
        panelIzquierdo.add(panelFormulario);
        panelIzquierdo.add(Box.createRigidArea(new Dimension(0, 15)));
        panelIzquierdo.add(btnAgregarPartida);
        
        // Panel Derecho - Tabla de Transacciones
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);
        
        JLabel lblTabla = new JLabel("📋 Registro de Transacciones (12)");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columnas = {"Fecha", "Tipo", "Categoría", "Descripción", "Monto", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones
            }
        };
        
        tablaTransacciones = new JTable(modeloTabla);
        tablaTransacciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaTransacciones.setRowHeight(40);
        tablaTransacciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaTransacciones.getTableHeader().setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollTabla = new JScrollPane(tablaTransacciones);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panelDerecho.add(lblTabla, BorderLayout.NORTH);
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        
        // Agregar paneles al contenido
        panelContenido.add(panelIzquierdo, BorderLayout.WEST);
        panelContenido.add(panelDerecho, BorderLayout.CENTER);
        
        // Agregar todo al panel principal
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(240, 242, 235));
        panelTop.add(panelHeader, BorderLayout.NORTH);
        panelTop.add(panelTabs, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelTop, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JButton crearBotonTab(String texto, boolean activo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        
        if (activo) {
            btn.setBackground(new Color(140, 160, 140));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(200, 215, 200));
            btn.setForeground(new Color(80, 80, 80));
        }
        
        return btn;
    }
    
    private JPanel crearPanelResumen(String titulo, String subtitulo, String tipo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 250, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        JLabel lblMonto;
        if (tipo.equals("green")) {
            lblMonto = new JLabel("9800.00 US$");
            lblMonto.setForeground(new Color(0, 150, 0));
            lblTotalIngresos = lblMonto;
        } else if (tipo.equals("red")) {
            lblMonto = new JLabel("-1730.00 US$");
            lblMonto.setForeground(new Color(200, 0, 0));
            lblTotalGastos = lblMonto;
        } else {
            lblMonto = new JLabel("8070.00 US$");
            lblMonto.setForeground(new Color(0, 120, 200));
            lblBalance = lblMonto;
        }
        
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(120, 120, 120));
        
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblMonto);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblSub);
        
        return panel;
    }
    
    private JTextField crearCampoFormulario(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBackground(new Color(200, 220, 200));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private void agregarPartida() {
        String fecha = txtFecha.getText().trim();
        String referencia = txtReferencia.getText().trim();
        String tipo = txtTipo.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String monto = txtMonto.getText().trim();
        
        if (fecha.isEmpty() || tipo.isEmpty() || monto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete al menos: Fecha, Tipo y Monto",
                "Campos obligatorios",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Guardar en base de datos
        if (guardarPartida(fecha, referencia, tipo, categoria, descripcion, monto)) {
            JOptionPane.showMessageDialog(this,
                "Partida agregada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarFormulario();
            cargarDatos();
        }
    }
    
    private boolean guardarPartida(String fecha, String referencia, String tipo, String categoria, String descripcion, String monto) {
        // Aquí implementarías el guardado en la base de datos
        // Por ahora solo agregamos a la tabla
        
        Object[] fila = {fecha, tipo, categoria, descripcion, monto, "🗑️"};
        modeloTabla.addRow(fila);
        
        return true;
    }
    
    private void cargarDatos() {
        // Aquí cargarías los datos desde la base de datos
        // Por ahora datos de ejemplo
        modeloTabla.setRowCount(0);
        
        modeloTabla.addRow(new Object[]{"14/12/2024", "Ingreso", "Ventas", "Venta de productos enero - primera quincena", "+2800.00 US$", "🗑️"});
        modeloTabla.addRow(new Object[]{"12/12/2024", "Gasto", "Materia prima", "Compra de materiales para producción", "-800.00 US$", "🗑️"});
        modeloTabla.addRow(new Object[]{"12/12/2024", "Ingreso", "Servicios", "Consultoría técnica cliente ABC", "+1200.00 US$", "🗑️"});
    }
    
    private void limpiarFormulario() {
        txtReferencia.setText("");
        txtTipo.setText("");
        txtCategoria.setText("");
        txtDescripcion.setText("");
        txtMonto.setText("");
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            login loginWindow = new login();
            loginWindow.setVisible(true);
            dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Principal("Juan", "Pérez", "Administrador").setVisible(true);
        });
    }
}