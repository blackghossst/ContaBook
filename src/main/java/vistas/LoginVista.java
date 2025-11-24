package vistas;

import Controlador.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginVista extends JFrame {
    
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JLabel lblRegistrar;
    private final LoginController controlador;
    
    public LoginVista() {
        controlador = new LoginController(this);
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Tu diseño original, intacto
        setTitle("ContaBook - Iniciar Sesión");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout());
        
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(50, 50, 30, 50));
        
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
        
        JLabel lblSubtitulo = new JLabel("\"Sistema de contabilidad empresarial\"");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelSuperior.add(panelTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(lblSubtitulo);
        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(new Color(245, 245, 245));
        panelFormulario.setLayout(null);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        panelFormulario.setPreferredSize(new Dimension(600, 350));
        
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
        
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(60, 60, 60));
        lblUsuario.setBounds(80, 90, 440, 20);
        
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(80, 115, 440, 40);
        txtUsuario.setBackground(new Color(200, 220, 200));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(60, 60, 60));
        lblPassword.setBounds(80, 165, 440, 20);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(80, 190, 440, 40);
        txtPassword.setBackground(new Color(200, 220, 200));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Acción Enter
        txtPassword.addActionListener(e -> controlador.iniciarSesion(
                txtUsuario.getText().trim(), new String(txtPassword.getPassword())
        ));
        
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnIniciarSesion.setBounds(180, 250, 240, 45);
        btnIniciarSesion.setBackground(new Color(140, 160, 140));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Eventos del botón
        btnIniciarSesion.addActionListener(e -> controlador.iniciarSesion(
                txtUsuario.getText().trim(), new String(txtPassword.getPassword())
        ));
        
        btnIniciarSesion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(120, 140, 120));
            }
            public void mouseExited(MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(140, 160, 140));
            }
        });
        
        JPanel panelRegistro = new JPanel();
        panelRegistro.setBackground(new Color(245, 245, 245));
        panelRegistro.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRegistro.setBounds(80, 305, 440, 30);
              
        panelFormulario.add(panelTituloForm);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(btnIniciarSesion);
        panelFormulario.add(panelRegistro);
        
        JPanel panelCentro = new JPanel();
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCentro.add(panelFormulario);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        add(panelPrincipal);
    }
    
    public void limpiarCampos() {
        txtPassword.setText("");
        txtUsuario.requestFocus();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }
}
