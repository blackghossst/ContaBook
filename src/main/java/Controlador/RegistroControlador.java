package controlador;

import conexion.Conexion;

import java.sql.*;
import javax.swing.JOptionPane;
import models.Usuario;

public class RegistroControlador {

    public boolean registrarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConexion();
            if (conn == null) return false;

            // Encriptar contraseña (si tu método existe)
            String passwordEncriptada = Conexion.encriptarPassword(usuario.getPassword());

            String sql = "INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getUsuario());
            stmt.setString(4, passwordEncriptada);
            stmt.setString(5, usuario.getRol());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean usuarioExiste(String usuario) {
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
}
