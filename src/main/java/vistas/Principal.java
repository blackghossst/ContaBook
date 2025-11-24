package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Conexion;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import vistas.FormularioPartida;
import vistas.Periodos;
import vistas.PortadaContaBook;
import vistas.Usuarios;


// Nuevas importaciones para reportes y cuentas
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Principal extends JFrame {
    
    // Definición de colores verdes consistentes
    private static final Color COLOR_VERDE_PRINCIPAL = new Color(140, 160, 140);
    private static final Color COLOR_VERDE_CLARO = new Color(220, 235, 220);
    private static final Color COLOR_VERDE_HOVER = new Color(200, 220, 200);
    private static final Color COLOR_VERDE_INACTIVO = new Color(200, 215, 200);
    private static final Color COLOR_VERDE_BORDE = new Color(180, 200, 180);
    private static final Color COLOR_VERDE_FONDO_PANEL = new Color(245, 250, 245);
    private static final Color COLOR_VERDE_FONDO_GENERAL = new Color(240, 242, 235);
    
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rolUsuario;
    private String usuarioActual;
    
    private JLabel lblTotalIngresos;
    private JLabel lblTotalGastos;
    private JLabel lblBalance;
    private JLabel lblContadorTransacciones;
    private JTable tablaTransacciones;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtFecha;
    private JTextField txtReferencia;
    private JTextField txtTipo;
    private JTextField txtCategoria;
    private JTextField txtMonto;
    private JTextArea txtDescripcion;
    private JButton btnAgregarPartida;
    private JButton btnSubirDocumento;
    private JLabel lblDocumento;
    private File archivoSeleccionado = null;
    
    // Variables para edición
    private int filaEditando = -1;
    private int idTransaccionEditando = -1;
    
    public Principal(String nombre, String apellido, String rol) {
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.rolUsuario = rol;
        this.usuarioActual = nombre + " " + apellido;
        
        inicializarComponentes();
        cargarDatos();
        actualizarResumen();
    }
    
    private void inicializarComponentes() {
        setTitle("ContaBook - Libro de Cuentas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(COLOR_VERDE_FONDO_GENERAL);
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== HEADER ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_VERDE_FONDO_GENERAL);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("ContaBook - Libro de Cuentas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.BLACK);
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setBackground(COLOR_VERDE_FONDO_GENERAL);
        
        JLabel lblUsuario = new JLabel(nombreUsuario + " " + apellidoUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblRol = new JLabel(rolUsuario);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(100, 100, 100));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(COLOR_VERDE_PRINCIPAL);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setBackground(COLOR_VERDE_FONDO_GENERAL);
        panelInfoUsuario.add(lblUsuario);
        panelInfoUsuario.add(lblRol);
        
        panelUsuario.add(panelInfoUsuario);
        panelUsuario.add(Box.createRigidArea(new Dimension(15, 0)));
        panelUsuario.add(btnCerrarSesion);
        
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);
        
        // ========== TABS ==========
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTabs.setBackground(COLOR_VERDE_FONDO_GENERAL);
        
        JButton btnPanelPrincipal = crearBotonTab("Panel Principal", true);
        JButton btnPeriodos = crearBotonTab("Períodos", false);
        JButton btnReportes = crearBotonTab("Reportes", false);
        JButton btnCuentas = crearBotonTab("Cuentas", false); // Nueva pestaña para cuentas
        JButton btnUsuarios = crearBotonTab("Usuarios", false);
        
        if (rolUsuario.equalsIgnoreCase("Usuario")) {
            btnPeriodos.setEnabled(false);
            btnPeriodos.setBackground(COLOR_VERDE_INACTIVO);
            btnReportes.setEnabled(false);
            btnReportes.setBackground(COLOR_VERDE_INACTIVO);
            btnCuentas.setEnabled(false);
            btnCuentas.setBackground(COLOR_VERDE_INACTIVO);
            btnUsuarios.setEnabled(false);
            btnUsuarios.setBackground(COLOR_VERDE_INACTIVO);
        } else if (rolUsuario.equalsIgnoreCase("Contador")) {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setBackground(COLOR_VERDE_INACTIVO);
        } else if (!rolUsuario.equalsIgnoreCase("Admin") && !rolUsuario.equalsIgnoreCase("Administrador")) {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setBackground(COLOR_VERDE_INACTIVO);
        }
        
        panelTabs.add(btnPanelPrincipal);
        panelTabs.add(btnPeriodos);
        btnPeriodos.addActionListener(e -> {
            Periodos ventanaPeriodos = new Periodos(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaPeriodos.setVisible(true);
        });
        panelTabs.add(btnReportes);
        btnReportes.addActionListener(e -> mostrarReportes()); // Acción para reportes
        panelTabs.add(btnCuentas);
        btnCuentas.addActionListener(e -> mostrarCuentas()); // Acción para cuentas
        panelTabs.add(btnUsuarios);
        btnUsuarios.addActionListener(e -> {
            Usuarios ventanaUsuarios = new Usuarios(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaUsuarios.setVisible(true);
            dispose();
        });
        
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
        // Botón desplegable con hover
        JButton btnFormTitulo = new JButton("+ Nueva Partida Contable");
        btnFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnFormTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnFormTitulo.setBackground(COLOR_VERDE_CLARO);
        btnFormTitulo.setForeground(Color.BLACK);
        btnFormTitulo.setFocusPainted(false);
        btnFormTitulo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFormTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnFormTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnFormTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Efecto hover
        btnFormTitulo.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnFormTitulo.setBackground(COLOR_VERDE_HOVER);
            }
            public void mouseExited(MouseEvent evt) {
                btnFormTitulo.setBackground(COLOR_VERDE_CLARO);
            }
        });
        
        // Acción para abrir formulario en nueva ventana
        btnFormTitulo.addActionListener(e -> {
            FormularioPartida formulario = new FormularioPartida(this, nombreUsuario, apellidoUsuario, rolUsuario);
            formulario.setVisible(true);
        });
        
        panelIzquierdo.add(panelResumen);
        panelIzquierdo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelIzquierdo.add(btnFormTitulo);
        
        // Panel Derecho - Tabla de Transacciones
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);
        
        lblContadorTransacciones = new JLabel("📋 Registro de Transacciones (0)");
        lblContadorTransacciones.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblContadorTransacciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columnas = {"ID", "Fecha", "Tipo", "Categoría", "Descripción", "Monto", "Documento", "Editar", "Eliminar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8; // Solo editar y eliminar
            }
        };
        
        tablaTransacciones = new JTable(modeloTabla);
        tablaTransacciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaTransacciones.setRowHeight(40);
        tablaTransacciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaTransacciones.getTableHeader().setBackground(new Color(245, 245, 245));
        
        // Ocultar columna ID
        tablaTransacciones.getColumnModel().getColumn(0).setMinWidth(0);
        tablaTransacciones.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaTransacciones.getColumnModel().getColumn(0).setWidth(0);
        
        // Agregar listener para los botones de la tabla
        tablaTransacciones.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaTransacciones.rowAtPoint(e.getPoint());
                int columna = tablaTransacciones.columnAtPoint(e.getPoint());
                
                if (fila >= 0) {
                    if (columna == 7) { // Editar
                        editarPartida(fila);
                    } else if (columna == 8) { // Eliminar
                        eliminarPartida(fila);
                    } else if (columna == 6) { // Documento
                        verDocumento(fila);
                    }
                }
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaTransacciones);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panelDerecho.add(lblContadorTransacciones, BorderLayout.NORTH);
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        
        panelContenido.add(panelIzquierdo, BorderLayout.WEST);
        panelContenido.add(panelDerecho, BorderLayout.CENTER);
        
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(COLOR_VERDE_FONDO_GENERAL);
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
            btn.setBackground(COLOR_VERDE_PRINCIPAL);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(COLOR_VERDE_INACTIVO);
            btn.setForeground(new Color(80, 80, 80));
        }
        
        return btn;
    }
    
    private JPanel crearPanelResumen(String titulo, String subtitulo, String tipo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_VERDE_FONDO_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_VERDE_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        JLabel lblMonto;
        if (tipo.equals("green")) {
            lblMonto = new JLabel("0.00 US$");
            lblMonto.setForeground(new Color(0, 150, 0));
            lblTotalIngresos = lblMonto;
        } else if (tipo.equals("red")) {
            lblMonto = new JLabel("-0.00 US$");
            lblMonto.setForeground(new Color(200, 0, 0));
            lblTotalGastos = lblMonto;
        } else {
            lblMonto = new JLabel("0.00 US$");
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
        txt.setBackground(COLOR_VERDE_HOVER);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_VERDE_BORDE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private void seleccionarDocumento() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar documento de comprobación");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Documentos (PDF, IMG, DOC)", "pdf", "jpg", "jpeg", "png", "doc", "docx");
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            lblDocumento.setText("📄 " + archivoSeleccionado.getName());
            lblDocumento.setForeground(new Color(0, 120, 0));
        }
    }
    
    private void agregarPartida() {
        if (!rolUsuario.equalsIgnoreCase("Usuario") && 
            !rolUsuario.equalsIgnoreCase("Admin") && 
            !rolUsuario.equalsIgnoreCase("Administrador") && 
            !rolUsuario.equalsIgnoreCase("Contador")) {
            JOptionPane.showMessageDialog(this,
                "No tiene permisos para agregar partidas",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
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
        
        if (!tipo.equalsIgnoreCase("Ingreso") && !tipo.equalsIgnoreCase("Gasto")) {
            JOptionPane.showMessageDialog(this,
                "El tipo debe ser 'Ingreso' o 'Gasto'",
                "Tipo inválido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double montoDouble = Double.parseDouble(monto);
            if (montoDouble <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El monto debe ser mayor a cero",
                    "Monto inválido",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
                } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El monto debe ser un número válido",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (guardarPartida(fecha, referencia, tipo, categoria, descripcion, monto)) {
            JOptionPane.showMessageDialog(this,
                "Partida agregada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarFormulario();
            cargarDatos();
            actualizarResumen();
        }
    }
    
    private boolean guardarPartida(String fecha, String referencia, String tipo, String categoria, String descripcion, String monto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            String sql = "INSERT INTO transacciones (fecha, referencia, tipo, categoria, descripcion, monto, usuario, documento) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fecha);
            stmt.setString(2, referencia);
            stmt.setString(3, tipo);
            stmt.setString(4, categoria);
            stmt.setString(5, descripcion);
            stmt.setDouble(6, Double.parseDouble(monto));
            stmt.setString(7, usuarioActual);
            
            if (archivoSeleccionado != null) {
                FileInputStream fis = new FileInputStream(archivoSeleccionado);
                stmt.setBinaryStream(8, fis, (int) archivoSeleccionado.length());
            } else {
                stmt.setNull(8, Types.BINARY);
            }
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la partida:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
            
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void cargarDatos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                return;
            }
            
            modeloTabla.setRowCount(0);
            
            String sql = "SELECT * FROM transacciones ORDER BY idtransaccion DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            int contador = 0;
            while (rs.next()) {
                int id = rs.getInt("idtransaccion");
                String fecha = rs.getString("fecha");
                String tipo = rs.getString("tipo");
                String categoria = rs.getString("categoria");
                String descripcion = rs.getString("descripcion");
                double monto = rs.getDouble("monto");
                byte[] documento = rs.getBytes("documento");
                
                String montoFormateado;
                if (tipo.equalsIgnoreCase("Ingreso")) {
                    montoFormateado = "+" + String.format("%.2f", monto) + " US$";
                } else {
                    montoFormateado = "-" + String.format("%.2f", monto) + " US$";
                }
                
                String docIcono = (documento != null && documento.length > 0) ? "📄" : "-";
                
                modeloTabla.addRow(new Object[]{
                    id,
                    fecha, 
                    tipo, 
                    categoria, 
                    descripcion, 
                    montoFormateado,
                    docIcono,
                    "✏️",
                    "🗑️"
                });
                
                contador++;
            }
            
            lblContadorTransacciones.setText("📋 Registro de Transacciones (" + contador + ")");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar transacciones:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarResumen() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                return;
            }
            
            // Calcular total de ingresos
            String sqlIngresos = "SELECT COALESCE(SUM(monto), 0) as total FROM transacciones WHERE tipo = 'Ingreso'";
            stmt = conn.prepareStatement(sqlIngresos);
            rs = stmt.executeQuery();
            
            double totalIngresos = 0;
            if (rs.next()) {
                totalIngresos = rs.getDouble("total");
            }
            rs.close();
            stmt.close();
            
            // Calcular total de gastos
            String sqlGastos = "SELECT COALESCE(SUM(monto), 0) as total FROM transacciones WHERE tipo = 'Gasto'";
            stmt = conn.prepareStatement(sqlGastos);
            rs = stmt.executeQuery();
            
            double totalGastos = 0;
            if (rs.next()) {
                totalGastos = rs.getDouble("total");
            }
            
            // Calcular balance
            double balance = totalIngresos - totalGastos;
            
            // Actualizar labels
            lblTotalIngresos.setText(String.format("%.2f US$", totalIngresos));
            lblTotalGastos.setText(String.format("-%.2f US$", totalGastos));
            lblBalance.setText(String.format("%.2f US$", balance));
            
            // Cambiar color del balance según sea positivo o negativo
            if (balance >= 0) {
                lblBalance.setForeground(new Color(0, 150, 0));
            } else {
                lblBalance.setForeground(new Color(200, 0, 0));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void editarPartida(int fila) {
        // Verificar permisos
        if (!rolUsuario.equalsIgnoreCase("Admin") && 
            !rolUsuario.equalsIgnoreCase("Administrador") && 
            !rolUsuario.equalsIgnoreCase("Contador")) {
            JOptionPane.showMessageDialog(this,
                "No tiene permisos para editar partidas",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener el ID de la transacción
        int idTransaccion = (int) modeloTabla.getValueAt(fila, 0);
        
        // Abrir el formulario en modo edición
        FormularioPartida ff = new FormularioPartida( this, 
            nombreUsuario, 
            apellidoUsuario, 
            rolUsuario, 
            idTransaccion  // Pasar el ID para edición
        );
        ff.setVisible(true);
    }
    
    private void eliminarPartida(int fila) {
        // Pedir contraseña de confirmación
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this,
            passwordField,
            "Ingrese su contraseña para confirmar la eliminación:",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String passwordIngresada = new String(passwordField.getPassword());
        
        if (passwordIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar su contraseña para eliminar",
                "Contraseña requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar contraseña
        if (!validarPasswordUsuario(passwordIngresada)) {
            JOptionPane.showMessageDialog(this,
                "Contraseña incorrecta",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar esta partida?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        int idTransaccion = (int) modeloTabla.getValueAt(fila, 0);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String sql = "DELETE FROM transacciones WHERE idtransaccion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTransaccion);
            
            int filasEliminadas = stmt.executeUpdate();
            
            if (filasEliminadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Partida eliminada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                cargarDatos();
                actualizarResumen();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar la partida:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean validarPasswordUsuario(String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                return false;
            }
            
            String passwordEncriptada = Conexion.encriptarPassword(password);
            
            String sql = "SELECT * FROM usuario WHERE nombre = ? AND apellido = ? AND contraseña = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, apellidoUsuario);
            stmt.setString(3, passwordEncriptada);
            
            rs = stmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void verDocumento(int fila) {
        int idTransaccion = (int) modeloTabla.getValueAt(fila, 0);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT documento FROM transacciones WHERE idtransaccion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTransaccion);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                byte[] documento = rs.getBytes("documento");
                
                if (documento == null || documento.length == 0) {
                    JOptionPane.showMessageDialog(this,
                        "Esta transacción no tiene documento adjunto",
                        "Sin documento",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Guardar temporalmente y abrir
                File tempFile = File.createTempFile("documento_", ".pdf");
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(documento);
                fos.close();
                
                Desktop.getDesktop().open(tempFile);
                
                // El archivo temporal se eliminará al cerrar la aplicación
                tempFile.deleteOnExit();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir el documento:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void limpiarFormulario() {
        txtReferencia.setText("");
        txtTipo.setText("");
        txtCategoria.setText("");
        txtDescripcion.setText("");
        txtMonto.setText("");
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        archivoSeleccionado = null;
        lblDocumento.setText("Ningún archivo seleccionado");
        lblDocumento.setForeground(new Color(120, 120, 120));
        
        // Resetear modo edición
        filaEditando = -1;
        idTransaccionEditando = -1;
        btnAgregarPartida.setText("+ Agregar Partida");
        btnAgregarPartida.setBackground(COLOR_VERDE_PRINCIPAL);
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            PortadaContaBook pb= new PortadaContaBook();
            pb.setVisible(true);
            dispose();
        }
    }
    
    // ========== NUEVOS MÉTODOS PARA REPORTES Y CUENTAS ==========
    
    private void mostrarReportes() {
        JFrame ventanaReportes = new JFrame("Reportes - ContaBook");
        ventanaReportes.setSize(800, 600);
        ventanaReportes.setLocationRelativeTo(this);
        ventanaReportes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panelReportes = new JPanel(new BorderLayout());
        panelReportes.setBackground(Color.WHITE);
        panelReportes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTituloReportes = new JLabel("Reportes Financieros");
        lblTituloReportes.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloReportes.setHorizontalAlignment(SwingConstants.CENTER);
        panelReportes.add(lblTituloReportes, BorderLayout.NORTH);
        
        // Panel central con opciones
        JPanel panelOpciones = new JPanel(new GridLayout(2, 1, 20, 20));
        panelOpciones.setBackground(Color.WHITE);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        JButton btnBalanceGeneral = new JButton("Balance General");
        btnBalanceGeneral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnBalanceGeneral.setBackground(COLOR_VERDE_PRINCIPAL);
        btnBalanceGeneral.setForeground(Color.WHITE);
        btnBalanceGeneral.setFocusPainted(false);
        btnBalanceGeneral.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBalanceGeneral.addActionListener(e -> mostrarBalanceGeneral());
        
        JButton btnLibroMayor = new JButton("Libro Mayor");
        btnLibroMayor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLibroMayor.setBackground(COLOR_VERDE_PRINCIPAL);
        btnLibroMayor.setForeground(Color.WHITE);
        btnLibroMayor.setFocusPainted(false);
        btnLibroMayor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLibroMayor.addActionListener(e -> mostrarLibroMayor());
        
        panelOpciones.add(btnBalanceGeneral);
        panelOpciones.add(btnLibroMayor);
        
        panelReportes.add(panelOpciones, BorderLayout.CENTER);
        
        ventanaReportes.add(panelReportes);
        ventanaReportes.setVisible(true);
    }
    
    private void mostrarBalanceGeneral() {
    JFrame ventanaBalance = new JFrame("Balance General - ContaBook");
    ventanaBalance.setSize(600, 400);
    ventanaBalance.setLocationRelativeTo(this);
    ventanaBalance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    JPanel panelBalance = new JPanel(new BorderLayout());
    panelBalance.setBackground(Color.WHITE);
    panelBalance.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Título
    JLabel lblTitulo = new JLabel("Balance General");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    panelBalance.add(lblTitulo, BorderLayout.NORTH);
    
    // Contenido del balance
    JTextArea txtBalance = new JTextArea();
    txtBalance.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    txtBalance.setEditable(false);
    txtBalance.setBackground(new Color(245, 245, 245));
    txtBalance.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Calcular datos del balance desde BD
    StringBuilder balance = new StringBuilder();
    balance.append("ACTIVOS:\n");
    balance.append("  Caja y Bancos: ").append(calcularTotalPorCategoria("Caja")).append(" US$\n");
    balance.append("  Inventario: ").append(calcularTotalPorCategoria("Inventario")).append(" US$\n");
    balance.append("Total Activos: ").append(calcularTotalActivos()).append(" US$\n\n");
    
    balance.append("PASIVOS:\n");
    balance.append("  Proveedores: ").append(calcularTotalPorCategoria("Proveedores")).append(" US$\n");
    balance.append("Total Pasivos: ").append(calcularTotalPasivos()).append(" US$\n\n");
    
    balance.append("PATRIMONIO:\n");
    balance.append("  Capital: ").append(calcularTotalIngresos() - calcularTotalGastos()).append(" US$\n");
    balance.append("Total Patrimonio: ").append(calcularTotalIngresos() - calcularTotalGastos()).append(" US$\n");
    
    txtBalance.setText(balance.toString());
    
    JScrollPane scrollBalance = new JScrollPane(txtBalance);
    panelBalance.add(scrollBalance, BorderLayout.CENTER);
    
    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout());
    JButton btnDescargarPDF = new JButton("📄 Descargar PDF");
    btnDescargarPDF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDescargarPDF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btnDescargarPDF.setBackground(COLOR_VERDE_PRINCIPAL);
    btnDescargarPDF.setForeground(Color.WHITE);
    btnDescargarPDF.setFocusPainted(false);
    btnDescargarPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnDescargarPDF.addActionListener(e -> GeneradorPDF.descargarBalancePDF(balance.toString(), nombreUsuario, apellidoUsuario, rolUsuario, this));
    panelBotones.add(btnDescargarPDF);
    
    panelBalance.add(panelBotones, BorderLayout.SOUTH);
    
    ventanaBalance.add(panelBalance);
    ventanaBalance.setVisible(true);
}
    
private void mostrarLibroMayor() {
    JFrame ventanaLibro = new JFrame("Libro Mayor - ContaBook");
    ventanaLibro.setSize(800, 600);
    ventanaLibro.setLocationRelativeTo(this);
    ventanaLibro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    JPanel panelLibro = new JPanel(new BorderLayout());
    panelLibro.setBackground(Color.WHITE);
    panelLibro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Título
    JLabel lblTitulo = new JLabel("Libro Mayor");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    panelLibro.add(lblTitulo, BorderLayout.NORTH);
    
    // Tabla del libro mayor
    String[] columnasLibro = {"Fecha", "Descripción", "Débito", "Crédito", "Saldo"};
    DefaultTableModel modeloLibro = new DefaultTableModel(columnasLibro, 0);
    
    // Cargar datos del libro mayor desde BD
    cargarDatosLibroMayor(modeloLibro);
    
    JTable tablaLibro = new JTable(modeloLibro);
    tablaLibro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaLibro.setRowHeight(30);
    tablaLibro.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    tablaLibro.getTableHeader().setBackground(new Color(245, 245, 245));
    
    JScrollPane scrollLibro = new JScrollPane(tablaLibro);
    panelLibro.add(scrollLibro, BorderLayout.CENTER);
    
    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout());
    JButton btnDescargarPDF = new JButton("📄 Descargar PDF");
    btnDescargarPDF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btnDescargarPDF.setBackground(COLOR_VERDE_PRINCIPAL);
    btnDescargarPDF.setForeground(Color.WHITE);
    btnDescargarPDF.setFocusPainted(false);
    btnDescargarPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnDescargarPDF.addActionListener(e -> GeneradorPDF.descargarLibroPDF(modeloLibro, nombreUsuario, apellidoUsuario, rolUsuario, this));
    panelBotones.add(btnDescargarPDF);
    
    panelLibro.add(panelBotones, BorderLayout.SOUTH);
    
    ventanaLibro.add(panelLibro);
    ventanaLibro.setVisible(true);
}

    
    private void mostrarCuentas() {
    JFrame ventanaCuentas = new JFrame("Cuentas T - ContaBook");
    ventanaCuentas.setExtendedState(JFrame.MAXIMIZED_BOTH); // ⬅️ Pantalla completa
    ventanaCuentas.setLocationRelativeTo(this);
    ventanaCuentas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaCuentas.setVisible(true);
        
        JPanel panelCuentas = new JPanel(new BorderLayout());
        panelCuentas.setBackground(Color.WHITE);
        panelCuentas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Cuentas T");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelCuentas.add(lblTitulo, BorderLayout.NORTH);
        
        // Panel para agregar nueva cuenta
        JPanel panelAgregarCuenta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregarCuenta.setBackground(Color.WHITE);
        panelAgregarCuenta.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JTextField txtNuevaCuenta = new JTextField(20);
        txtNuevaCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnAgregarCuenta = new JButton("Agregar Cuenta");
        btnAgregarCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAgregarCuenta.setBackground(COLOR_VERDE_PRINCIPAL);
        btnAgregarCuenta.setForeground(Color.WHITE);
        btnAgregarCuenta.setFocusPainted(false);
        btnAgregarCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarCuenta.addActionListener(e -> agregarCuenta(txtNuevaCuenta.getText().trim()));
        
        panelAgregarCuenta.add(new JLabel("Nueva Cuenta:"));
        panelAgregarCuenta.add(txtNuevaCuenta);
        panelAgregarCuenta.add(btnAgregarCuenta);
        
        panelCuentas.add(panelAgregarCuenta, BorderLayout.NORTH);
        
        // Lista de cuentas con formato T
        JPanel panelListaCuentas = new JPanel();
        panelListaCuentas.setLayout(new BoxLayout(panelListaCuentas, BoxLayout.Y_AXIS));
        panelListaCuentas.setBackground(Color.WHITE);
        
        JScrollPane scrollCuentas = new JScrollPane(panelListaCuentas);
        scrollCuentas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Cargar cuentas existentes desde BD
        cargarCuentasT(panelListaCuentas);
        
        panelCuentas.add(scrollCuentas, BorderLayout.CENTER);
        
        ventanaCuentas.add(panelCuentas);
        ventanaCuentas.setVisible(true);
    }
    
    private void agregarCuenta(String nombreCuenta) {
        if (nombreCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para la cuenta", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "INSERT INTO cuentas (nombre, saldo) VALUES (?, 0.00)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreCuenta);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cuenta '" + nombreCuenta + "' agregada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Recargar cuentas (necesitarías refrescar la ventana)
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void cargarCuentasT(JPanel panel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT * FROM cuentas";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String nombreCuenta = rs.getString("nombre");
                JPanel panelCuentaT = crearPanelCuentaT(nombreCuenta);
                panel.add(panelCuentaT);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cuentas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private JPanel crearPanelCuentaT(String nombreCuenta) {
        JPanel panelCuenta = new JPanel(new BorderLayout());
        panelCuenta.setBackground(Color.WHITE);
        panelCuenta.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), nombreCuenta));
        panelCuenta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Tabla para débitos y créditos
        String[] columnas = {"Fecha", "Descripción", "Gasto", "Ingreso"};
        DefaultTableModel modeloCuenta = new DefaultTableModel(columnas, 0);
        
        // Cargar movimientos de la cuenta desde BD
        cargarMovimientosCuenta(modeloCuenta, nombreCuenta);
        
        JTable tablaCuenta = new JTable(modeloCuenta);
        tablaCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaCuenta.setRowHeight(25);
        
        JScrollPane scrollCuenta = new JScrollPane(tablaCuenta);
        panelCuenta.add(scrollCuenta, BorderLayout.CENTER);
        
        // Saldo
        JLabel lblSaldo = new JLabel("Saldo: " + calcularSaldoCuenta(nombreCuenta) + " US$");
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSaldo.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelCuenta.add(lblSaldo, BorderLayout.SOUTH);
        
        return panelCuenta;
    }
    
    private void cargarMovimientosCuenta(DefaultTableModel modelo, String cuenta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT fecha, descripcion, tipo, monto FROM transacciones WHERE categoria = ? ORDER BY fecha";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cuenta);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String fecha = rs.getString("fecha");
                String descripcion = rs.getString("descripcion");
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                
                if (tipo.equalsIgnoreCase("Ingreso")) {
                    modelo.addRow(new Object[]{fecha, descripcion, "", String.format("%.2f", monto)});
                } else {
                    modelo.addRow(new Object[]{fecha, descripcion, String.format("%.2f", monto), ""});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private double calcularSaldoCuenta(String cuenta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT COALESCE(SUM(CASE WHEN tipo = 'Ingreso' THEN monto ELSE -monto END), 0) as saldo FROM transacciones WHERE categoria = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cuenta);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0.00;
    }
    
    private void cargarDatosLibroMayor(DefaultTableModel modelo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT fecha, descripcion, tipo, monto FROM transacciones ORDER BY fecha";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            double saldoAcumulado = 0;
            while (rs.next()) {
                String fecha = rs.getString("fecha");
                String descripcion = rs.getString("descripcion");
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                
                if (tipo.equalsIgnoreCase("Ingreso")) {
                    saldoAcumulado += monto;
                    modelo.addRow(new Object[]{fecha, descripcion, "", String.format("%.2f", monto), String.format("%.2f", saldoAcumulado)});
                } else {
                    saldoAcumulado -= monto;
                    modelo.addRow(new Object[]{fecha, descripcion, String.format("%.2f", monto), "", String.format("%.2f", saldoAcumulado)});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libro mayor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private double calcularTotalPorCategoria(String categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT COALESCE(SUM(CASE WHEN tipo = 'Ingreso' THEN monto ELSE -monto END), 0) as total FROM transacciones WHERE categoria = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0.00;
    }
    
    private double calcularTotalActivos() {
        // Suma de categorías de activos (ejemplo: Caja, Inventario)
        return calcularTotalPorCategoria("Caja") + calcularTotalPorCategoria("Inventario");
    }
    
    private double calcularTotalPasivos() {
        // Suma de categorías de pasivos (ejemplo: Proveedores)
        return calcularTotalPorCategoria("Proveedores");
    }
    
    private double calcularTotalIngresos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT COALESCE(SUM(monto), 0) as total FROM transacciones WHERE tipo = 'Ingreso'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0.00;
    }
    
    private double calcularTotalGastos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            String sql = "SELECT COALESCE(SUM(monto), 0) as total FROM transacciones WHERE tipo = 'Gasto'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0.00;
    }
    
}
