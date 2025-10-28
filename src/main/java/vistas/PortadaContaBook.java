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

public class PortadaContaBook extends JFrame {
    
    public PortadaContaBook() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuración de la ventana
        setTitle("ContaBook - Sistema de Registro Contable");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con fondo BLANCO (como el login)
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout());
        
        // Panel central con contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(true);
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(60, 50, 40, 50));
        
        // Icono/Logo - Cambiado a calculadora como el login
        JLabel lblLogo = new JLabel("🧮");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setForeground(Color.BLACK); // Cambiado a negro
        
        // Título
        JLabel lblTitulo = new JLabel("ContaBook");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(Color.BLACK); // Cambiado a negro
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema de Registro Contable");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setForeground(new Color(100, 100, 100)); // Gris oscuro
        
        // Panel de descripción con fondo gris claro (como el login)
        JPanel panelDescripcion = new JPanel();
        panelDescripcion.setBackground(new Color(245, 245, 245)); // Gris claro
        panelDescripcion.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panelDescripcion.setLayout(new BoxLayout(panelDescripcion, BoxLayout.Y_AXIS));
        panelDescripcion.setMaximumSize(new Dimension(600, 250));
        
        JLabel lblDescTitulo = new JLabel("¿Qué es ContaBook?");
        lblDescTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDescTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDescTitulo.setForeground(new Color(60, 60, 60)); // Gris oscuro
        
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setText(
            "ContaBook es una aplicación de escritorio diseñada para facilitar el registro\n" +
"y gestión de libros de cuentas de forma local. Permite a usuarios y empresas llevar\n" +
"un control detallado de sus transacciones contables de manera\n" +
"eficiente, organizada y segura sin depender de un navegador web.\n\n" +
"✓ Registro de ingresos y egresos\n" +
"✓ Gestión de cuentas contables\n" +
"✓ Generación de reportes financieros\n" +
"✓ Interfaz intuitiva y fácil de usar"
        );
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setForeground(new Color(80, 80, 80)); // Gris oscuro
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botón de inicio - VERDE como el login
        JButton btnIngresar = new JButton("INGRESAR AL SISTEMA");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(300, 45));
        btnIngresar.setBackground(new Color(140, 160, 140)); // Verde del login
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para el botón - Verde más oscuro
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(120, 140, 120)); // Verde hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(140, 160, 140)); // Verde normal
            }
        });
        
        // Acción del botón
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginVista lo = new LoginVista();
                lo.setVisible(true);
                
                // Cerrar la ventana de portada
                dispose();
            }
        });
        
        // Agregar componentes al panel de descripción
        panelDescripcion.add(lblDescTitulo);
        panelDescripcion.add(Box.createRigidArea(new Dimension(0, 15)));
        panelDescripcion.add(txtDescripcion);
        
        // Agregar componentes al panel de contenido
        panelContenido.add(lblLogo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));
        panelContenido.add(lblSubtitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));
        panelContenido.add(panelDescripcion);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 25)));
        panelContenido.add(btnIngresar);
        
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    public static void main(String[] args) {
        // Establecer el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PortadaContaBook().setVisible(true);
            }
        });
    }
}