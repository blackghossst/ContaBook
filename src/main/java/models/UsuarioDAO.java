/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nemma
 */

import conexion.Conexion;
import java.sql.*;

public class UsuarioDAO {
    
    public boolean validarUsuario(String usuario, String password) {
        String sql = "SELECT * FROM usuario WHERE usuario = ? AND contraseña = ?";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String passwordEncriptada = Conexion.encriptarPassword(password);
            stmt.setString(1, usuario);
            stmt.setString(2, passwordEncriptada);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Si hay resultado, el usuario existe
            
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }

    public String[] obtenerDatosUsuario(String usuario) {
        String[] datos = new String[3]; // nombre, apellido, rol
        String sql = "SELECT nombre, apellido, rol FROM usuario WHERE usuario = ?";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                datos[0] = rs.getString("nombre");
                datos[1] = rs.getString("apellido");
                datos[2] = rs.getString("rol");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos del usuario: " + e.getMessage());
        }
        return datos;
    }
}
