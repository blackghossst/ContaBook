/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Conexion;

public class PerfilUsuario extends JDialog {
    
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rolUsuario;
    private String usuarioLogin;
    
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JTextField txtRol;
    private JPasswordField txtPasswordActual;
    private JPasswordField txtPasswordNueva;
    private JPasswordField txtPasswordConfirmar;
    
    private JButton btnActualizar;
    private JButton btnCambiarPassword;
    private JButton btnCerrar;
    
    private Principal ventanaPrincipal;
    
    public PerfilUsuario(Principal parent, String nombre, String apellido, String rol) {
        super(parent, "Mi Perfil", true);
        this.ventanaPrincipal = parent;
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.rolUsuario = rol;
        
        inicializarComponentes();
        cargarDatosUsuario();
    }
    
    private void inicializarComponentes() {
        setSize(700, 750);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // ========== HEADER ==========
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Avatar y título
        JPanel panelAvatar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAvatar.setBackground(Color.WHITE);
        
        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        panelAvatar.add(lblAvatar);
        
        JLabel lblTitulo = new JLabel(nombreUsuario + " " + apellidoUsuario);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(140, 160, 140));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel(rolUsuario);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelHeader.add(panelAvatar);
        panelHeader.add(Box.createRigidArea(new Dimension(0, 10)));
        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createRigidArea(new Dimension(0, 5)));
        panelHeader.add(lblSubtitulo);
        
        // ========== FORMULARIO ==========
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        
        // Sección: Información Personal
        JLabel lblSeccion1 = new JLabel("📋 Información Personal");
        lblSeccion1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeccion1.setForeground(new Color(140, 160, 140));
        lblSeccion1.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        lblSeccion1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(lblSeccion1);
        
        txtNombre = crearCampoFormulario(panelFormulario, "Nombre");
        txtApellido = crearCampoFormulario(panelFormulario, "Apellido");
        txtUsuario = crearCampoFormulario(panelFormulario, "Usuario");
        txtRol = crearCampoFormulario(panelFormulario, "Rol");
        txtRol.setEditable(false);
        txtRol.setBackground(new Color(230, 230, 230));
        
        // Botón actualizar información
        btnActualizar = new JButton("💾 Actualizar Información");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnActualizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnActualizar.setBackground(new Color(140, 160, 140));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnActualizar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnActualizar.setBackground(new Color(120, 140, 120));
            }
            public void mouseExited(MouseEvent evt) {
                btnActualizar.setBackground(new Color(140, 160, 140));
            }
        });
        
        btnActualizar.addActionListener(e -> actualizarInformacion());
        
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 10)));
        panelFormulario.add(btnActualizar);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Separador
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panelFormulario.add(separador);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Sección: Cambiar Contraseña
        JLabel lblSeccion2 = new JLabel("🔒 Cambiar Contraseña");
        lblSeccion2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeccion2.setForeground(new Color(140, 160, 140));
        lblSeccion2.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        lblSeccion2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(lblSeccion2);
        
        txtPasswordActual = crearCampoPassword(panelFormulario, "Contraseña Actual");
        txtPasswordNueva = crearCampoPassword(panelFormulario, "Nueva Contraseña");
        txtPasswordConfirmar = crearCampoPassword(panelFormulario, "Confirmar Nueva Contraseña");
        
        // Botón cambiar contraseña
        btnCambiarPassword = new JButton("🔑 Cambiar Contraseña");
        btnCambiarPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCambiarPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCambiarPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCambiarPassword.setBackground(new Color(200, 140, 60));
        btnCambiarPassword.setForeground(Color.WHITE);
        btnCambiarPassword.setFocusPainted(false);
        btnCambiarPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarPassword.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnCambiarPassword.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCambiarPassword.setBackground(new Color(180, 120, 40));
            }
            public void mouseExited(MouseEvent evt) {
                btnCambiarPassword.setBackground(new Color(200, 140, 60));
            }
        });
        
        btnCambiarPassword.addActionListener(e -> cambiarPassword());
        
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 10)));
        panelFormulario.add(btnCambiarPassword);
        
        // ========== BOTÓN CERRAR ==========
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setPreferredSize(new Dimension(200, 40));
        btnCerrar.setBackground(new Color(200, 200, 200));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCerrar.setBackground(new Color(180, 180, 180));
            }
            public void mouseExited(MouseEvent evt) {
                btnCerrar.setBackground(new Color(200, 200, 200));
            }
        });
        
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCerrar);
        
        // ========== AGREGAR TODO ==========
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JTextField crearCampoFormulario(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setBackground(new Color(200, 220, 200));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private JPasswordField crearCampoPassword(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField txt = new JPasswordField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setBackground(new Color(200, 220, 200));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private void cargarDatosUsuario() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM usuario WHERE nombre = ? AND apellido = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, apellidoUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtApellido.setText(rs.getString("apellido"));
                txtUsuario.setText(rs.getString("usuario"));
                txtRol.setText(rs.getString("rol"));
                usuarioLogin = rs.getString("usuario");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos del usuario:\n" + e.getMessage(),
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
    
    private void actualizarInformacion() {
        String nuevoNombre = txtNombre.getText().trim();
        String nuevoApellido = txtApellido.getText().trim();
        String nuevoUsuario = txtUsuario.getText().trim();
        
        if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "UPDATE usuario SET nombre = ?, apellido = ?, usuario = ? WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoApellido);
            stmt.setString(3, nuevoUsuario);
            stmt.setString(4, usuarioLogin);
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "✅ Información actualizada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar variables
                nombreUsuario = nuevoNombre;
                apellidoUsuario = nuevoApellido;
                usuarioLogin = nuevoUsuario;
                
                // Cerrar y reabrir para aplicar cambios en el dashboard
                dispose();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar información:\n" + e.getMessage(),
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
    
    private void cambiarPassword() {
        String passwordActual = new String(txtPasswordActual.getPassword());
        String passwordNueva = new String(txtPasswordNueva.getPassword());
        String passwordConfirmar = new String(txtPasswordConfirmar.getPassword());
        
        if (passwordActual.isEmpty() || passwordNueva.isEmpty() || passwordConfirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos de contraseña",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!passwordNueva.equals(passwordConfirmar)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas nuevas no coinciden",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (passwordNueva.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "La nueva contraseña debe tener al menos 6 caracteres",
                "Contraseña débil",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar contraseña actual
        if (!verificarPasswordActual(passwordActual)) {
            JOptionPane.showMessageDialog(this,
                "La contraseña actual es incorrecta",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar contraseña
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            
            String passwordEncriptada = Conexion.encriptarPassword(passwordNueva);
            
            String sql = "UPDATE usuario SET contraseña = ? WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, passwordEncriptada);
            stmt.setString(2, usuarioLogin);
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "✅ Contraseña actualizada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                txtPasswordActual.setText("");
                txtPasswordNueva.setText("");
                txtPasswordConfirmar.setText("");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cambiar contraseña:\n" + e.getMessage(),
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
    
    private boolean verificarPasswordActual(String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String passwordEncriptada = Conexion.encriptarPassword(password);
            
            String sql = "SELECT * FROM usuario WHERE usuario = ? AND contraseña = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuarioLogin);
            stmt.setString(2, passwordEncriptada);
            
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
}
