package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Conexion;

public class Usuarios extends JFrame {
    
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rolUsuario;
    
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalUsuarios;
    
    public Usuarios(String nombre, String apellido, String rol) {
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.rolUsuario = rol;
        
        inicializarComponentes();
        cargarUsuarios();
    }
    
    private void inicializarComponentes() {
        setTitle("ContaBook - Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(240, 242, 235));
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== HEADER ==========
        JPanel panelHeader = crearHeader();
        
        // ========== TABS ==========
        JPanel panelTabs = crearTabs();
        
        // ========== CONTENIDO ==========
        JPanel panelContenido = new JPanel(new BorderLayout(15, 15));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel Superior - Título y botón agregar
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        
        lblTotalUsuarios = new JLabel("👥 Usuarios Registrados (0)");
        lblTotalUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JButton btnNuevoUsuario = new JButton("+ Nuevo Usuario");
        btnNuevoUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevoUsuario.setBackground(new Color(140, 160, 140));
        btnNuevoUsuario.setForeground(Color.WHITE);
        btnNuevoUsuario.setFocusPainted(false);
        btnNuevoUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoUsuario.setPreferredSize(new Dimension(180, 40));
        btnNuevoUsuario.addActionListener(e -> abrirFormularioNuevoUsuario());
        
        panelSuperior.add(lblTotalUsuarios, BorderLayout.WEST);
        panelSuperior.add(btnNuevoUsuario, BorderLayout.EAST);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Tabla de usuarios
        JPanel panelTabla = crearPanelTabla();
        
        panelContenido.add(panelSuperior, BorderLayout.NORTH);
        panelContenido.add(panelTabla, BorderLayout.CENTER);
        
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(240, 242, 235));
        panelTop.add(panelHeader, BorderLayout.NORTH);
        panelTop.add(panelTabs, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelTop, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(240, 242, 235));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("ContaBook - Gestión de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.BLACK);
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setBackground(new Color(240, 242, 235));
        
        JLabel lblUsuario = new JLabel(nombreUsuario + " " + apellidoUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblRol = new JLabel(rolUsuario);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(100, 100, 100));
        
        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(140, 160, 140));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            Principal ventanaPrincipal = new Principal(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaPrincipal.setVisible(true);
            dispose();
        });
        
        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setBackground(new Color(240, 242, 235));
        panelInfoUsuario.add(lblUsuario);
        panelInfoUsuario.add(lblRol);
        
        panelUsuario.add(panelInfoUsuario);
        panelUsuario.add(Box.createRigidArea(new Dimension(15, 0)));
        panelUsuario.add(btnVolver);
        
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);
        
        return panelHeader;
    }
    
    private JPanel crearTabs() {
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTabs.setBackground(new Color(240, 242, 235));
        
        JButton btnPanelPrincipal = crearBotonTab("Panel Principal", false);
        JButton btnPeriodos = crearBotonTab("Períodos", false);
        JButton btnReportes = crearBotonTab("Reportes", false);
        JButton btnUsuarios = crearBotonTab("Usuarios", true);
        
        btnPanelPrincipal.addActionListener(e -> {
            Principal ventanaPrincipal = new Principal(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaPrincipal.setVisible(true);
            dispose();
        });
        
        btnPeriodos.addActionListener(e -> {
            Periodos ventanaPeriodos = new Periodos(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaPeriodos.setVisible(true);
            dispose();
        });
        
        btnReportes.addActionListener(e -> {
            Reportes ventanaReportes = new Reportes(nombreUsuario, apellidoUsuario, rolUsuario);
            ventanaReportes.setVisible(true);
            dispose();
        });
        
        panelTabs.add(btnPanelPrincipal);
        panelTabs.add(btnPeriodos);
        panelTabs.add(btnReportes);
        panelTabs.add(btnUsuarios);
        
        return panelTabs;
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
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String[] columnas = {"ID", "Nombre", "Apellido", "Usuario", "Rol", "Editar", "Eliminar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Solo editar y eliminar
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(45);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(new Color(140, 160, 140));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        // Ocultar columna ID
        tablaUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        tablaUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaUsuarios.getColumnModel().getColumn(0).setWidth(0);
        
        // Agregar listener para los botones
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaUsuarios.rowAtPoint(e.getPoint());
                int columna = tablaUsuarios.columnAtPoint(e.getPoint());
                
                if (fila >= 0) {
                    if (columna == 5) { // Editar
                        editarUsuario(fila);
                    } else if (columna == 6) { // Eliminar
                        eliminarUsuario(fila);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarUsuarios() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            modeloTabla.setRowCount(0);
            
            String sql = "SELECT * FROM usuario ORDER BY idusuario DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            int contador = 0;
            while (rs.next()) {
                int id = rs.getInt("idusuario");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String usuario = rs.getString("usuario");
                String rol = rs.getString("rol");
                
                modeloTabla.addRow(new Object[]{
                    id,
                    nombre,
                    apellido,
                    usuario,
                    rol,
                    "✏️",
                    "🗑️"
                });
                
                contador++;
            }
            
            lblTotalUsuarios.setText("👥 Usuarios Registrados (" + contador + ")");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar usuarios:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void abrirFormularioNuevoUsuario() {
        JDialog dialogo = new JDialog(this, "Nuevo Usuario", true);
        dialogo.setSize(450, 400);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Campos del formulario
        JTextField txtNombre = crearCampoTexto(panel, "Nombre:");
        JTextField txtApellido = crearCampoTexto(panel, "Apellido:");
        JTextField txtUsuario = crearCampoTexto(panel, "Usuario:");
        JPasswordField txtPassword = crearCampoPassword(panel, "Contraseña:");
        
        // ComboBox para Rol - SOLO ADMIN PUEDE EDITAR
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRol.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Usuario", "Contador", "Admin"});
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Solo Admin puede cambiar el rol
        if (rolUsuario.equalsIgnoreCase("Admin") || rolUsuario.equalsIgnoreCase("Administrador")) {
            cmbRol.setBackground(new Color(245, 245, 245));
            cmbRol.setEnabled(true);
        } else {
            cmbRol.setBackground(new Color(230, 230, 230));
            cmbRol.setEnabled(false);
            cmbRol.setSelectedItem("Usuario"); // Por defecto Usuario si no es Admin
        }
        
        panel.add(lblRol);
        panel.add(cmbRol);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(140, 160, 140));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(100, 35));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setBackground(new Color(180, 180, 180));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        
        btnGuardar.addActionListener(e -> {
            if (guardarUsuario(txtNombre.getText(), txtApellido.getText(), 
                              txtUsuario.getText(), new String(txtPassword.getPassword()), 
                              (String) cmbRol.getSelectedItem())) {
                dialogo.dispose();
                cargarUsuarios();
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelBotones);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    private JTextField crearCampoTexto(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBackground(new Color(245, 245, 245));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private JPasswordField crearCampoPassword(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JPasswordField txt = new JPasswordField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBackground(new Color(245, 245, 245));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private boolean guardarUsuario(String nombre, String apellido, String usuario, 
                                   String password, String rol) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || 
            usuario.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
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
            
            String passwordEncriptada = Conexion.encriptarPassword(password);
            
            String sql = "INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, usuario);
            stmt.setString(4, passwordEncriptada);
            stmt.setString(5, rol);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Usuario creado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(this,
                    "El nombre de usuario ya existe",
                    "Usuario duplicado",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar usuario:\n" + e.getMessage(),
                    "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
            
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    private void editarUsuario(int fila) {
        int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
        String nombreActual = (String) modeloTabla.getValueAt(fila, 1);
        String apellidoActual = (String) modeloTabla.getValueAt(fila, 2);
        String usuarioActual = (String) modeloTabla.getValueAt(fila, 3);
        String rolActual = (String) modeloTabla.getValueAt(fila, 4);
        
        JDialog dialogo = new JDialog(this, "Editar Usuario", true);
        dialogo.setSize(450, 450);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JTextField txtNombre = crearCampoTexto(panel, "Nombre:");
        txtNombre.setText(nombreActual);
        
        JTextField txtApellido = crearCampoTexto(panel, "Apellido:");
        txtApellido.setText(apellidoActual);
        
        JTextField txtUsuario = crearCampoTexto(panel, "Usuario:");
        txtUsuario.setText(usuarioActual);
        
        JPasswordField txtPassword = crearCampoPassword(panel, "Nueva Contraseña (dejar vacío para mantener):");
        
        // Rol - SOLO ADMIN PUEDE EDITAR
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRol.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panel.add(lblRol);
        
        if (rolUsuario.equalsIgnoreCase("Admin") || rolUsuario.equalsIgnoreCase("Administrador")) {
            // Admin puede cambiar el rol con un ComboBox
            JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Usuario", "Contador", "Admin"});
            cmbRol.setSelectedItem(rolActual);
            cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            cmbRol.setBackground(new Color(245, 245, 245));
            cmbRol.setEnabled(true);
            panel.add(cmbRol);
            
            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
            panelBotones.setBackground(Color.WHITE);
            panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnGuardar.setBackground(new Color(140, 160, 140));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFocusPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setPreferredSize(new Dimension(100, 35));
            
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnCancelar.setBackground(new Color(180, 180, 180));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.setPreferredSize(new Dimension(100, 35));
            
            btnGuardar.addActionListener(e -> {
                if (actualizarUsuario(idUsuario, txtNombre.getText(), txtApellido.getText(), 
                                     txtUsuario.getText(), new String(txtPassword.getPassword()), 
                                     (String) cmbRol.getSelectedItem())) {
                    dialogo.dispose();
                    cargarUsuarios();
                }
            });
            
            btnCancelar.addActionListener(e -> dialogo.dispose());
            
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);
            
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(panelBotones);
            
        } else {
            // Otros usuarios NO pueden cambiar el rol
            JTextField txtRol = new JTextField(rolActual);
            txtRol.setFont(new Font("Segoe UI", Font.BOLD, 13));
            txtRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txtRol.setBackground(new Color(230, 230, 230));
            txtRol.setEditable(false);
            txtRol.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            panel.add(txtRol);
            
            // Nota informativa
            JLabel lblNota = new JLabel("⚠️ Solo administradores pueden cambiar roles");
            lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblNota.setForeground(new Color(200, 100, 0));
            lblNota.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            panel.add(lblNota);
            
            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
            panelBotones.setBackground(Color.WHITE);
            panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnGuardar.setBackground(new Color(140, 160, 140));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFocusPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setPreferredSize(new Dimension(100, 35));
            
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnCancelar.setBackground(new Color(180, 180, 180));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.setPreferredSize(new Dimension(100, 35));
            
            btnGuardar.addActionListener(e -> {
                if (actualizarUsuario(idUsuario, txtNombre.getText(), txtApellido.getText(), 
                                     txtUsuario.getText(), new String(txtPassword.getPassword()), 
                                     rolActual)) { // Mantiene el rol actual
                    dialogo.dispose();
                    cargarUsuarios();
                }
            });
            
            btnCancelar.addActionListener(e -> dialogo.dispose());
            
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);
            
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(panelBotones);
        }
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    private boolean actualizarUsuario(int id, String nombre, String apellido, String usuario, 
                                     String password, String rol) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || usuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos obligatorios",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
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
            
            String sql;
            if (password.trim().isEmpty()) {
                // No actualizar contraseña
                sql = "UPDATE usuario SET nombre = ?, apellido = ?, usuario = ?, rol = ? WHERE idusuario = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, usuario);
                stmt.setString(4, rol);
                stmt.setInt(5, id);
            } else {
                // Actualizar con nueva contraseña
                String passwordEncriptada = Conexion.encriptarPassword(password);
                sql = "UPDATE usuario SET nombre = ?, apellido = ?, usuario = ?, contraseña = ?, rol = ? WHERE idusuario = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, usuario);
                stmt.setString(4, passwordEncriptada);
                stmt.setString(5, rol);
                stmt.setInt(6, id);
            }
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Usuario actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(this,
                    "El nombre de usuario ya existe",
                    "Usuario duplicado",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar usuario:\n" + e.getMessage(),
                    "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
            
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    private void eliminarUsuario(int fila) {
        int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(fila, 1);
        String apellidoUsuario = (String) modeloTabla.getValueAt(fila, 2);
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al usuario:\n" + nombreUsuario + " " + apellidoUsuario + "?\n\nEsta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Pedir contraseña
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this,
            passwordField,
            "Ingrese su contraseña para confirmar:",
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
        
        // Validar contraseña del usuario actual
        if (!validarPasswordUsuario(passwordIngresada)) {
            JOptionPane.showMessageDialog(this,
                "Contraseña incorrecta",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
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
            
            String sql = "DELETE FROM usuario WHERE idusuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            int filasEliminadas = stmt.executeUpdate();
            
            if (filasEliminadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Usuario eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                cargarUsuarios();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar usuario:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            try {
                if (stmt != null) stmt.close();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Usuarios("Admin", "Sistema", "Administrador").setVisible(true);
        });
    }
}