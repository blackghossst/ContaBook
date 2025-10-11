package vistas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Conexion;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;

public class FormularioPartida extends JDialog {
    
    private JTextField txtFecha;
    private JTextField txtReferencia;
    private JTextField txtTipo;
    private JComboBox<String> comboTipo;
    private JTextField txtCategoria;
    private JTextField txtMonto;
    private JTextArea txtDescripcion;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnSubirDocumento;
    private JLabel lblDocumento;
    
    private File archivoSeleccionado = null;
    private String usuarioActual;
    private String rolUsuario;
    private Principal ventanaPrincipal;
    
    private boolean modoEdicion = false;
    private int idTransaccionEditar = -1;
    
    // Constructor para AGREGAR nueva partida
    public FormularioPartida(Principal parent, String nombre, String apellido, String rol) {
        this(parent, nombre, apellido, rol, -1);
    }
    
    // Constructor para EDITAR partida existente
    public FormularioPartida(Principal parent, String nombre, String apellido, String rol, int idTransaccion) {
        super(parent, idTransaccion > 0 ? "Editar Partida Contable" : "Nueva Partida Contable", true);
        this.ventanaPrincipal = parent;
        this.usuarioActual = nombre + " " + apellido;
        this.rolUsuario = rol;
        this.idTransaccionEditar = idTransaccion;
        this.modoEdicion = (idTransaccion > 0);
        
        inicializarComponentes();
        
        if (modoEdicion) {
            cargarDatosEdicion();
        }
    }
    
      private void inicializarComponentes() {
        setSize(600, 800);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Título
        JLabel lblTitulo = new JLabel("📝 Nueva Partida Contable");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(140, 160, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panel del formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        // Fecha
        txtFecha = crearCampoFormulario(panelFormulario, "Fecha *");
        if (!modoEdicion) {
            txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        }
        
        // Referencia
        txtReferencia = crearCampoFormulario(panelFormulario, "Referencia");
        
        // Tipo
        JLabel lblTipo = new JLabel("Tipo *");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTipo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelFormulario.add(lblTipo);
        
        String[] tipos = {"Seleccione...", "Ingreso", "Gasto"};
        comboTipo = new JComboBox<>(tipos);
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboTipo.setBackground(new Color(200, 220, 200));
        comboTipo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Guardar selección en txtTipo
        txtTipo = new JTextField();
        comboTipo.addActionListener(e -> {
            String seleccion = (String) comboTipo.getSelectedItem();
            if (!seleccion.equals("Seleccione...")) {
                txtTipo.setText(seleccion);
            }
        });
        
        panelFormulario.add(comboTipo);
        
        // Categoría
        txtCategoria = crearCampoFormulario(panelFormulario, "Categoría");
        
        // Descripción
        JLabel lblDesc = new JLabel("Descripción *");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDesc.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelFormulario.add(lblDesc);
        
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(new Color(200, 220, 200));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panelFormulario.add(scrollDesc);
        
        // Monto
        txtMonto = crearCampoFormulario(panelFormulario, "Monto (US$) *");
        
        // Documento
        JLabel lblDocTitulo = new JLabel("Documento de comprobación (opcional)");
        lblDocTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDocTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panelFormulario.add(lblDocTitulo);
        
        btnSubirDocumento = new JButton("📎 Seleccionar archivo");
        btnSubirDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSubirDocumento.setBackground(new Color(220, 235, 220));
        btnSubirDocumento.setFocusPainted(false);
        btnSubirDocumento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnSubirDocumento.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubirDocumento.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        btnSubirDocumento.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnSubirDocumento.setBackground(new Color(200, 220, 200));
            }
            public void mouseExited(MouseEvent evt) {
                btnSubirDocumento.setBackground(new Color(220, 235, 220));
            }
        });
        
        btnSubirDocumento.addActionListener(e -> seleccionarDocumento());
        panelFormulario.add(btnSubirDocumento);
        
        lblDocumento = new JLabel(modoEdicion ? "Documento actual conservado" : "Ningún archivo seleccionado");
        lblDocumento.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblDocumento.setForeground(new Color(120, 120, 120));
        lblDocumento.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        panelFormulario.add(lblDocumento);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelBotones.setBackground(Color.WHITE);
        
        String textoBoton = modoEdicion ? "💾 Actualizar Partida" : "💾 Guardar Partida";
        btnGuardar = new JButton(textoBoton);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setPreferredSize(new Dimension(200, 45));
        btnGuardar.setBackground(modoEdicion ? new Color(200, 140, 60) : new Color(140, 160, 140));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (modoEdicion) {
                    btnGuardar.setBackground(new Color(180, 120, 40));
                } else {
                    btnGuardar.setBackground(new Color(120, 140, 120));
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (modoEdicion) {
                    btnGuardar.setBackground(new Color(200, 140, 60));
                } else {
                    btnGuardar.setBackground(new Color(140, 160, 140));
                }
            }
        });
        
        btnGuardar.addActionListener(e -> {
            if (modoEdicion) {
                actualizarPartida();
            } else {
                guardarPartida();
            }
        });
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setPreferredSize(new Dimension(200, 45));
        btnCancelar.setBackground(new Color(200, 100, 100));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCancelar.setBackground(new Color(180, 80, 80));
            }
            public void mouseExited(MouseEvent evt) {
                btnCancelar.setBackground(new Color(200, 100, 100));
            }
        });
        
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        // Agregar todo al panel principal
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void cargarDatosEdicion() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones WHERE idtransaccion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTransaccionEditar);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                txtFecha.setText(rs.getString("fecha"));
                txtReferencia.setText(rs.getString("referencia"));
                
                String tipo = rs.getString("tipo");
                txtTipo.setText(tipo);
                if (tipo.equals("Ingreso")) {
                    comboTipo.setSelectedIndex(1);
                } else if (tipo.equals("Gasto")) {
                    comboTipo.setSelectedIndex(2);
                }
                
                txtCategoria.setText(rs.getString("categoria"));
                txtDescripcion.setText(rs.getString("descripcion"));
                txtMonto.setText(String.valueOf(rs.getDouble("monto")));
                
                // Verificar si tiene documento
                byte[] documento = rs.getBytes("documento");
                if (documento != null && documento.length > 0) {
                    lblDocumento.setText("📄 Documento actual conservado (puede reemplazar)");
                    lblDocumento.setForeground(new Color(0, 120, 0));
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos:\n" + e.getMessage(),
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
    
    private JTextField crearCampoFormulario(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setBackground(new Color(200, 220, 200));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private void seleccionarDocumento() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar documento de comprobación");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Documentos (PDF, IMG, DOC)", "pdf", "jpg", "jpeg", "png", "doc", "docx");
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            lblDocumento.setText("📄 " + archivoSeleccionado.getName() + " (nuevo)");
            lblDocumento.setForeground(new Color(0, 120, 0));
        }
    }
    
    private void actualizarPartida() {
        String fecha = txtFecha.getText().trim();
        String referencia = txtReferencia.getText().trim();
        String tipo = txtTipo.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String monto = txtMonto.getText().trim();
        
        // Validaciones
        if (fecha.isEmpty() || tipo.isEmpty() || descripcion.isEmpty() || monto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos obligatorios (*)",
                "Campos obligatorios",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!tipo.equalsIgnoreCase("Ingreso") && !tipo.equalsIgnoreCase("Gasto")) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un tipo válido (Ingreso o Gasto)",
                "Tipo inválido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double montoDouble = Double.parseDouble(monto);
            if (montoDouble <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El monto debe ser mayor a cero",
                    "Monto inválido",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El monto debe ser un número válido",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConexion();
            
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se pudo conectar a la base de datos",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String sql;
            if (archivoSeleccionado != null) {
                // Actualizar incluyendo el documento
                sql = "UPDATE transacciones SET fecha = ?, referencia = ?, tipo = ?, " +
                      "categoria = ?, descripcion = ?, monto = ?, documento = ? WHERE idtransaccion = ?";
            } else {
                // Actualizar sin cambiar el documento
                sql = "UPDATE transacciones SET fecha = ?, referencia = ?, tipo = ?, " +
                      "categoria = ?, descripcion = ?, monto = ? WHERE idtransaccion = ?";
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fecha);
            stmt.setString(2, referencia);
            stmt.setString(3, tipo);
            stmt.setString(4, categoria);
            stmt.setString(5, descripcion);
            stmt.setDouble(6, Double.parseDouble(monto));
            
            if (archivoSeleccionado != null) {
                FileInputStream fis = new FileInputStream(archivoSeleccionado);
                stmt.setBinaryStream(7, fis, (int) archivoSeleccionado.length());
                stmt.setInt(8, idTransaccionEditar);
            } else {
                stmt.setInt(7, idTransaccionEditar);
            }
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "✅ Partida actualizada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar la ventana principal
                ventanaPrincipal.cargarDatos();
                ventanaPrincipal.actualizarResumen();
                
                // Cerrar el formulario
                dispose();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar la partida:\n" + e.getMessage(),
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
    
    private void guardarPartida() {
        // ... (mantén el código existente de guardarPartida())
        // El código es el mismo que ya tenías
    }
}