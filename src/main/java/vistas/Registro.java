/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.Conexion;

/**
 * @author nemma
 */
public class Registro extends JFrame {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JTextField txtRol;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegistrar;
    private JLabel lblYaTienesCuenta;

    public Registro() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración de la ventana
        setTitle("ContaBook - Registro");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout());

        // Panel superior con logo y título
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(40, 50, 20, 50));

        // Logo y título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JLabel lblIcono = new JLabel("🧮");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));

        JLabel lblTitulo = new JLabel("ContaBook");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        lblTitulo.setForeground(Color.BLACK);

        panelTitulo.add(lblIcono);
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo);

        // Panel del formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(new Color(245, 245, 245));
        panelFormulario.setLayout(null);
        panelFormulario.setPreferredSize(new Dimension(600, 600));

        // Título del formulario
        JPanel panelTituloForm = new JPanel();
        panelTituloForm.setBackground(new Color(245, 245, 245));
        panelTituloForm.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTituloForm.setBounds(80, 25, 440, 35);

        JLabel lblIconoRegistro = new JLabel("📝");
        lblIconoRegistro.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));

        JLabel lblTituloForm = new JLabel("Registro");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTituloForm.setForeground(Color.BLACK);

        panelTituloForm.add(lblIconoRegistro);
        panelTituloForm.add(lblTituloForm);

        // Campos del formulario
        int yPos = 75;
        int spacing = 70;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombre.setForeground(new Color(60, 60, 60));
        lblNombre.setBounds(80, yPos, 440, 18);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNombre.setBounds(80, yPos + 20, 440, 35);
        txtNombre.setBackground(new Color(200, 220, 200));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        yPos += spacing;

        // Apellido
        JLabel lblApellido = new JLabel("Apellido");
        lblApellido.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblApellido.setForeground(new Color(60, 60, 60));
        lblApellido.setBounds(80, yPos, 440, 18);

        txtApellido = new JTextField();
        txtApellido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtApellido.setBounds(80, yPos + 20, 440, 35);
        txtApellido.setBackground(new Color(200, 220, 200));
        txtApellido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        yPos += spacing;

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(60, 60, 60));
        lblUsuario.setBounds(80, yPos, 440, 18);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario.setBounds(80, yPos + 20, 440, 35);
        txtUsuario.setBackground(new Color(200, 220, 200));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        yPos += spacing;

        // Rol
        JLabel lblRol = new JLabel("Rol (Admin, Usuario, Contador)");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRol.setForeground(new Color(60, 60, 60));
        lblRol.setBounds(80, yPos, 440, 18);

        txtRol = new JTextField();
        txtRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRol.setBounds(80, yPos + 20, 440, 35);
        txtRol.setBackground(new Color(200, 220, 200));
        txtRol.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtRol.setText("Usuario");
        yPos += spacing;

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(60, 60, 60));
        lblPassword.setBounds(80, yPos, 440, 18);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setBounds(80, yPos + 20, 440, 35);
        txtPassword.setBackground(new Color(200, 220, 200));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        yPos += spacing;

        // Confirmar contraseña
        JLabel lblConfirmPassword = new JLabel("Confirmar Contraseña");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblConfirmPassword.setForeground(new Color(60, 60, 60));
        lblConfirmPassword.setBounds(80, yPos, 440, 18);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConfirmPassword.setBounds(80, yPos + 20, 440, 35);
        txtConfirmPassword.setBackground(new Color(200, 220, 200));
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Botón registrar
        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegistrar.setBounds(180, yPos + 70, 240, 42);
        btnRegistrar.setBackground(new Color(140, 160, 140));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        btnRegistrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnRegistrar.setBackground(new Color(120, 140, 120));
            }

            public void mouseExited(MouseEvent evt) {
                btnRegistrar.setBackground(new Color(140, 160, 140));
            }
        });

        // Acción del botón
        btnRegistrar.addActionListener(e -> registrarUsuario());

        // Panel de login
        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(new Color(245, 245, 245));
        panelLogin.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelLogin.setBounds(80, yPos + 120, 440, 25);

        JLabel lblPregunta = new JLabel("¿Ya tienes una cuenta? ");
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPregunta.setForeground(new Color(100, 100, 100));

        lblYaTienesCuenta = new JLabel("Inicia sesión aquí");
        lblYaTienesCuenta.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblYaTienesCuenta.setForeground(new Color(120, 140, 120));
        lblYaTienesCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblYaTienesCuenta.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                lblYaTienesCuenta.setForeground(new Color(100, 120, 100));
            }

            public void mouseExited(MouseEvent evt) {
                lblYaTienesCuenta.setForeground(new Color(120, 140, 120));
            }

            public void mouseClicked(MouseEvent evt) {
                abrirLogin();
            }
        });

        panelLogin.add(lblPregunta);
        panelLogin.add(lblYaTienesCuenta);

        // Agregar componentes
        panelFormulario.add(panelTituloForm);
        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblApellido);
        panelFormulario.add(txtApellido);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblRol);
        panelFormulario.add(txtRol);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(lblConfirmPassword);
        panelFormulario.add(txtConfirmPassword);
        panelFormulario.add(btnRegistrar);
        panelFormulario.add(panelLogin);

        // Centrar formulario
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(panelFormulario);

        // Añadir paneles principales
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    // -------------------------- MÉTODOS --------------------------

    private void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String rol = txtRol.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() ||
            rol.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden",
                    "Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtConfirmPassword.setText("");
            txtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres",
                    "Contraseña débil", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioExiste(usuario)) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso",
                    "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (guardarEnBaseDatos(nombre, apellido, usuario, rol, password)) {
            JOptionPane.showMessageDialog(this, "¡Registro exitoso!\nBienvenido " + nombre + " " + apellido,
                    "Registro completado", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            abrirLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario.\nIntente nuevamente",
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean usuarioExiste(String usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion();
            if (conn == null) return false;

            String sql = "SELECT COUNT(*) FROM usuario WHERE usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

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

    private boolean guardarEnBaseDatos(String nombre, String apellido, String usuario, String rol, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConexion();
            if (conn == null) return false;

            String passwordEncriptada = Conexion.encriptarPassword(password);

            String sql = "INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, usuario);
            stmt.setString(4, passwordEncriptada);
            stmt.setString(5, rol);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtUsuario.setText("");
        txtRol.setText("Usuario");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtNombre.requestFocus();
    }

    private void abrirLogin() {
        LoginVista ventanaLogin = new LoginVista();
        ventanaLogin.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Registro().setVisible(true));
    }

    public Object getTxtNombre() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
