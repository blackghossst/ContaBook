/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.SQLException;

import java.security.MessageDigest;

import java.sql.SQLException;

public class Conexion {
    
    // Datos de conexión para PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/Contabook";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    
    private static Connection conexion = null;
    
    // Método para obtener la conexión
    public static Connection getConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a PostgreSQL");
            return conexion;
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Error: Driver de PostgreSQL no encontrado\n" + e.getMessage(),
                "Error de Driver",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error al conectar con la base de datos\n" + e.getMessage(),
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    // Método para cerrar la conexión
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // ========== AGREGA ESTE MÉTODO COMPLETO ==========
    public static String encriptarPassword(String password) {
        try {
            // Crear instancia de MessageDigest con algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Obtener el hash de la contraseña
            byte[] hash = md.digest(password.getBytes());
            
            // Convertir el hash a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al encriptar contraseña: " + e.getMessage());
            e.printStackTrace();
            // Si falla la encriptación, devolver la contraseña tal cual (NO RECOMENDADO en producción)
            return password;
        }
    }
    
    // Método para probar la conexión
    public static void main(String[] args) {
        Connection conn = getConexion();
        if (conn != null) {
            System.out.println("✓ Conexión establecida correctamente");
            
            // Probar encriptación
            String passwordPrueba = "123456";
            String passwordEncriptada = encriptarPassword(passwordPrueba);
            System.out.println("Password original: " + passwordPrueba);
            System.out.println("Password encriptada: " + passwordEncriptada);
            
            cerrarConexion();
        } else {
            System.out.println("✗ No se pudo establecer la conexión");
        }
    }
}