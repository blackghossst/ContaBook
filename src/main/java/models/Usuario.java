package models;

import conexion.Conexion;
import java.sql.*;

public class Usuario {
    private String nombre;
    private String apellido;
    private String usuario;
    private String rol;
    private String password;

    public Usuario(String nombre, String apellido, String usuario, String rol, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.rol = rol;
        this.password = password;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getUsuario() { return usuario; }
    public String getRol() { return rol; }
    public String getPassword() { return password; }

    // --- MÉTODOS DE BASE DE DATOS ---
    public static boolean existeUsuario(String usuario) {
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM usuario WHERE usuario = ?")) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean registrar() {
        String sql = "INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String passwordEncriptada = Conexion.encriptarPassword(password);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, usuario);
            stmt.setString(4, passwordEncriptada);
            stmt.setString(5, rol);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}
