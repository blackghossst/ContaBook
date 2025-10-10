/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author nemma
 */


import javax.swing.JOptionPane;
import models.UsuarioDAO;
import vistas.Principal;
import vistas.Registro;
import vistas.LoginVista;

public class LoginController {
    
    private final UsuarioDAO usuarioDAO;
    private final LoginVista vista;
    
    public LoginController(LoginVista vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public void iniciarSesion(String usuario, String password) {
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (usuarioDAO.validarUsuario(usuario, password)) {
            String[] datosUsuario = usuarioDAO.obtenerDatosUsuario(usuario);
            
            Principal dashboard = new Principal(
                datosUsuario[0],  // nombre
                datosUsuario[1],  // apellido
                datosUsuario[2]   // rol
            );
            dashboard.setVisible(true);
            vista.dispose();
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            vista.limpiarCampos();
        }
    }
    
    public void abrirRegistro() {
        Registro r = new Registro();
        r.setVisible(true);
        vista.dispose();
    }
}

