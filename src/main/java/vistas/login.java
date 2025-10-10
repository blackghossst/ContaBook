/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

/**
 *
 * @author nemma
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.Conexion;

public class login extends JFrame {
    
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JLabel lblRegistrar;
    
    public login() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuración de la ventana
        setTitle("ContaBook - Iniciar Sesión");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con fondo blanco
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout());
        
        // Panel superior con logo y título
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(50, 50, 30, 50));
        
        // Logo y título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        JLabel lblIcono = new JLabel("🧮");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel lblTitulo = new JLabel("ContaBook");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitulo.setForeground(Color.BLACK);
        
        panelTitulo.add(lblIcono);
        panelTitulo.add(lblTitulo);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("\"Sistema de contabilidad empresarial\"");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelSuperior.add(panelTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(lblSubtitulo);
        
        // Panel del formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(new Color(245, 245, 245));
        panelFormulario.setLayout(null);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        panelFormulario.setPreferredSize(new Dimension(600, 350));
        
        // Título del formulario
        JPanel panelTituloForm = new JPanel();
        panelTituloForm.setBackground(new Color(245, 245, 245));
        panelTituloForm.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTituloForm.setBounds(80, 30, 440, 40);
        
        JLabel lblIconoLogin = new JLabel("📋");
        lblIconoLogin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel lblTituloForm = new JLabel("Iniciar Sesión");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloForm.setForeground(Color.BLACK);
        
        panelTituloForm.add(lblIconoLogin);
        panelTituloForm.add(lblTituloForm);
        
        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(60, 60, 60));
        lblUsuario.setBounds(80, 90, 440, 20);
        
        // Campo de texto Usuario
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(80, 115, 440, 40);
        txtUsuario.setBackground(new Color(200, 220, 200));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(60, 60, 60));
        lblPassword.setBounds(80, 165, 440, 20);
        
        // Campo de contraseña
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(80, 190, 440, 40);
        txtPassword.setBackground(new Color(200, 220, 200));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Listener para Enter en el campo de contraseña
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        
        // Botón Iniciar Sesión
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnIniciarSesion.setBounds(180, 250, 240, 45);
        btnIniciarSesion.setBackground(new Color(140, 160, 140));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover del botón
        btnIniciarSesion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(120, 140, 120));
            }
            public void mouseExited(MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(140, 160, 140));
            }
        });
        
        // Acción del botón
        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        
        // Panel para el texto de registro
        JPanel panelRegistro = new JPanel();
        panelRegistro.setBackground(new Color(245, 245, 245));
        panelRegistro.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRegistro.setBounds(80, 305, 440, 30);
        
        JLabel lblPregunta = new JLabel("¿No tienes una cuenta? ");
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPregunta.setForeground(new Color(100, 100, 100));
        
        lblRegistrar = new JLabel("Regístrate aquí");
        lblRegistrar.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRegistrar.setForeground(new Color(120, 140, 120));
        lblRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para el link
        lblRegistrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                lblRegistrar.setForeground(new Color(100, 120, 100));
            }
            public void mouseExited(MouseEvent evt) {
                lblRegistrar.setForeground(new Color(120, 140, 120));
            }
            public void mouseClicked(MouseEvent evt) {
                abrirRegistro();
            }
        });
        
        panelRegistro.add(lblPregunta);
        panelRegistro.add(lblRegistrar);
        
        // Agregar componentes al formulario
        panelFormulario.add(panelTituloForm);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(btnIniciarSesion);
        panelFormulario.add(panelRegistro);
        
        // Centrar el formulario
        JPanel panelCentro = new JPanel();
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCentro.add(panelFormulario);
        
        // Agregar paneles al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private void iniciarSesion() {
    String usuario = txtUsuario.getText().trim();
    String password = new String(txtPassword.getPassword());
    
    if (usuario.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor, complete todos los campos",
            "Campos vacíos",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    if (validarUsuario(usuario, password)) {
        String[] datosUsuario = obtenerDatosUsuario(usuario);
        
        // Abrir dashboard con validación de roles
        Principal dashboard = new Principal(
            datosUsuario[0],  // nombre
            datosUsuario[1],  // apellido
            datosUsuario[2]   // rol
        );
        dashboard.setVisible(true);
        dispose();
        
    } else {
        JOptionPane.showMessageDialog(this,
            "Usuario o contraseña incorrectos",
            "Error de autenticación",
            JOptionPane.ERROR_MESSAGE);
        
        txtPassword.setText("");
        txtUsuario.requestFocus();
    }
}
    // Método para validar usuario con la base de datos
    private boolean validarUsuario(String usuario, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Obtener conexión
            conn = Conexion.getConexion();
            
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Encriptar la contraseña para compararla
            String passwordEncriptada = Conexion.encriptarPassword(password);
            
            // Preparar consulta SQL
            String sql = "SELECT * FROM usuario WHERE usuario = ? AND contraseña = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, passwordEncriptada);
            
            // Ejecutar consulta
            rs = stmt.executeQuery();
            
            // Si encuentra un resultado, el usuario existe
            return rs.next();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al validar usuario:\n" + e.getMessage(),
                "Error de BD",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
            
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método para obtener datos adicionales del usuario
    private String[] obtenerDatosUsuario(String usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String[] datos = new String[3]; // nombre, apellido, rol
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT nombre, apellido, rol FROM usuario WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                datos[0] = rs.getString("nombre");
                datos[1] = rs.getString("apellido");
                datos[2] = rs.getString("rol");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            datos[0] = "";
            datos[1] = "";
            datos[2] = "";
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return datos;
    }
    
    private void abrirRegistro() {
        Registro Ro = new Registro();
        Ro.setVisible(true);
        dispose();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
            }
        });
    }
}