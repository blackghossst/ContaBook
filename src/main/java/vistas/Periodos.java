package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Conexion;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;

public class Periodos extends JFrame {
    
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rolUsuario;
    
    private JTable tablaPeriodos;
    private DefaultTableModel modeloTabla;
    
    // Filtros
    private JComboBox<String> comboTipoFiltro;
    private JPanel panelFiltroPersonalizado;
    private JDateChooser dateDesde;
    private JDateChooser dateHasta;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboAnio;
    private JSpinner spinnerDias;
    private JButton btnFiltrar;
    private JButton btnLimpiar;
    
    private JLabel lblTotalIngresos;
    private JLabel lblTotalGastos;
    private JLabel lblBalance;
    private JLabel lblTransacciones;
    
    public Periodos(String nombre, String apellido, String rol) {
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.rolUsuario = rol;
        
        inicializarComponentes();
        cargarTodasTransacciones();
    }
    
    private void inicializarComponentes() {
        setTitle("ContaBook - Períodos");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(240, 242, 235));
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== HEADER ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(240, 242, 235));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("📅 Períodos y Filtros Contables");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.BLACK);
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setBackground(new Color(240, 242, 235));
        
        JLabel lblUsuario = new JLabel(nombreUsuario + " " + apellidoUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblRol = new JLabel(rolUsuario);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(100, 100, 100));
        
        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setBackground(new Color(140, 160, 140));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVolver.addActionListener(e -> dispose());
        
        JPanel panelInfoUsuario = new JPanel();
        panelInfoUsuario.setLayout(new BoxLayout(panelInfoUsuario, BoxLayout.Y_AXIS));
        panelInfoUsuario.setBackground(new Color(240, 242, 235));
        panelInfoUsuario.add(lblUsuario);
        panelInfoUsuario.add(lblRol);
        
        panelUsuario.add(panelInfoUsuario);
        panelUsuario.add(Box.createRigidArea(new Dimension(15, 0)));
        panelUsuario.add(btnVolver);
        
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(panelUsuario, BorderLayout.EAST);
        
        // ========== PANEL FILTROS ==========
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Selector de tipo de filtro
        JPanel panelTipoFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelTipoFiltro.setBackground(Color.WHITE);
        
        JLabel lblTipoFiltro = new JLabel("Tipo de filtro:");
        lblTipoFiltro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] tiposFiltro = {
            "Ver todas las transacciones",
            "Filtrar por rango de fechas",
            "Filtrar por últimos N días",
            "Filtrar por año",
            "Filtrar por tipo (Ingreso/Gasto)"
        };
        comboTipoFiltro = new JComboBox<>(tiposFiltro);
        comboTipoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboTipoFiltro.setPreferredSize(new Dimension(300, 35));
        comboTipoFiltro.setBackground(new Color(200, 220, 200));
        
        comboTipoFiltro.addActionListener(e -> cambiarTipoFiltro());
        
        panelTipoFiltro.add(lblTipoFiltro);
        panelTipoFiltro.add(comboTipoFiltro);
        
        // Panel para filtros personalizados
        panelFiltroPersonalizado = new JPanel();
        panelFiltroPersonalizado.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltroPersonalizado.setBackground(new Color(245, 250, 245));
        panelFiltroPersonalizado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panelFiltroPersonalizado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Inicializar componentes de filtro
        inicializarComponentesFiltro();
        
        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnFiltrar = new JButton("🔍 Aplicar Filtro");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFiltrar.setPreferredSize(new Dimension(150, 35));
        btnFiltrar.setBackground(new Color(140, 160, 140));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        
        btnLimpiar = new JButton("🔄 Limpiar Filtros");
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setPreferredSize(new Dimension(150, 35));
        btnLimpiar.setBackground(new Color(100, 120, 180));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnLimpiar);
        
        panelFiltros.add(panelTipoFiltro);
        panelFiltros.add(Box.createRigidArea(new Dimension(0, 10)));
        panelFiltros.add(panelFiltroPersonalizado);
        panelFiltros.add(panelBotones);
        
        // ========== CONTENIDO ==========
        JPanel panelContenido = new JPanel(new BorderLayout(15, 15));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel Izquierdo - Resumen
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(300, 0));
        
        JLabel lblResumenTitulo = new JLabel("📊 Resumen del Filtro");
        lblResumenTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResumenTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblResumenTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel panelResumen = new JPanel(new GridLayout(4, 1, 10, 10));
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        panelResumen.add(crearPanelResumen("Total Ingresos", "0.00 US$", "green"));
        panelResumen.add(crearPanelResumen("Total Gastos", "-0.00 US$", "red"));
        panelResumen.add(crearPanelResumen("Balance", "0.00 US$", "blue"));
        panelResumen.add(crearPanelResumen("Transacciones", "0", "gray"));
        
        panelIzquierdo.add(lblResumenTitulo);
        panelIzquierdo.add(panelResumen);
        
        // Panel Derecho - Tabla
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);
        
        JLabel lblTabla = new JLabel("📋 Resultado de Transacciones");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columnas = {"Fecha", "Tipo", "Categoría", "Descripción", "Monto", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPeriodos = new JTable(modeloTabla);
        tablaPeriodos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPeriodos.setRowHeight(40);
        tablaPeriodos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaPeriodos.getTableHeader().setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollTabla = new JScrollPane(tablaPeriodos);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panelDerecho.add(lblTabla, BorderLayout.NORTH);
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);
        
        panelContenido.add(panelIzquierdo, BorderLayout.WEST);
        panelContenido.add(panelDerecho, BorderLayout.CENTER);
        
        // Agregar todo
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(240, 242, 235));
        panelTop.add(panelHeader, BorderLayout.NORTH);
        panelTop.add(panelFiltros, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelTop, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private void inicializarComponentesFiltro() {
        // Rango de fechas
        dateDesde = new JDateChooser();
        dateDesde.setPreferredSize(new Dimension(150, 30));
        dateDesde.setDateFormatString("dd/MM/yyyy");
        
        dateHasta = new JDateChooser();
        dateHasta.setPreferredSize(new Dimension(150, 30));
        dateHasta.setDateFormatString("dd/MM/yyyy");
        dateHasta.setDate(new java.util.Date());
        
        // Tipo (Ingreso/Gasto)
        String[] tipos = {"Todos", "Ingreso", "Gasto"};
        comboTipo = new JComboBox<>(tipos);
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboTipo.setPreferredSize(new Dimension(150, 30));
        comboTipo.setBackground(new Color(200, 220, 200));
        
        // Año
        String[] anios = generarAnios();
        comboAnio = new JComboBox<>(anios);
        comboAnio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboAnio.setPreferredSize(new Dimension(120, 30));
        comboAnio.setBackground(new Color(200, 220, 200));
        
        // Spinner para días
        SpinnerNumberModel modeloDias = new SpinnerNumberModel(7, 1, 365, 1);
        spinnerDias = new JSpinner(modeloDias);
        spinnerDias.setPreferredSize(new Dimension(100, 30));
        spinnerDias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Ocultar por defecto
        panelFiltroPersonalizado.setVisible(false);
    }
    
    private void cambiarTipoFiltro() {
        panelFiltroPersonalizado.removeAll();
        panelFiltroPersonalizado.setVisible(true);
        
        int seleccion = comboTipoFiltro.getSelectedIndex();
        
        switch (seleccion) {
            case 0: // Ver todas
                panelFiltroPersonalizado.setVisible(false);
                cargarTodasTransacciones();
                break;
                
            case 1: // Rango de fechas
                panelFiltroPersonalizado.add(new JLabel("Desde:"));
                panelFiltroPersonalizado.add(dateDesde);
                panelFiltroPersonalizado.add(new JLabel("Hasta:"));
                panelFiltroPersonalizado.add(dateHasta);
                break;
                
            case 2: // Últimos N días
                panelFiltroPersonalizado.add(new JLabel("Últimos"));
                panelFiltroPersonalizado.add(spinnerDias);
                panelFiltroPersonalizado.add(new JLabel("días"));
                break;
                
            case 3: // Por año
                panelFiltroPersonalizado.add(new JLabel("Año:"));
                panelFiltroPersonalizado.add(comboAnio);
                break;
                
            case 4: // Por tipo
                panelFiltroPersonalizado.add(new JLabel("Tipo:"));
                panelFiltroPersonalizado.add(comboTipo);
                break;
        }
        
        panelFiltroPersonalizado.revalidate();
        panelFiltroPersonalizado.repaint();
    }
    
    private void aplicarFiltro() {
        int seleccion = comboTipoFiltro.getSelectedIndex();
        
        switch (seleccion) {
            case 0:
                cargarTodasTransacciones();
                break;
            case 1:
                filtrarPorRangoFechas();
                break;
            case 2:
                filtrarPorUltimosDias();
                break;
            case 3:
                filtrarPorAnio();
                break;
            case 4:
                filtrarPorTipo();
                break;
        }
    }
    
    private void filtrarPorRangoFechas() {
        if (dateDesde.getDate() == null || dateHasta.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione ambas fechas",
                "Fechas requeridas",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaDesde = sdf.format(dateDesde.getDate());
        String fechaHasta = sdf.format(dateHasta.getDate());
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones WHERE fecha_registro BETWEEN ?::date AND ?::date ORDER BY fecha_registro DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fechaDesde);
            stmt.setString(2, fechaHasta);
            
            rs = stmt.executeQuery();
            cargarResultados(rs);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }
    
    private void filtrarPorUltimosDias() {
        int dias = (Integer) spinnerDias.getValue();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones WHERE fecha_registro >= CURRENT_DATE - INTERVAL '" + dias + " days' ORDER BY fecha_registro DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            cargarResultados(rs);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }
    
    private void filtrarPorAnio() {
        String anioSeleccionado = (String) comboAnio.getSelectedItem();
        
        if (anioSeleccionado.equals("Todos")) {
            cargarTodasTransacciones();
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones WHERE EXTRACT(YEAR FROM fecha_registro) = ? ORDER BY fecha_registro DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(anioSeleccionado));
            
            rs = stmt.executeQuery();
            cargarResultados(rs);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }
    
    private void filtrarPorTipo() {
        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        
        if (tipoSeleccionado.equals("Todos")) {
            cargarTodasTransacciones();
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones WHERE tipo = ? ORDER BY fecha_registro DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipoSeleccionado);
            
            rs = stmt.executeQuery();
            cargarResultados(rs);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }
    
    private void cargarTodasTransacciones() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            
            String sql = "SELECT * FROM transacciones ORDER BY fecha_registro DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            cargarResultados(rs);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }
    
    private void cargarResultados(ResultSet rs) throws SQLException {
        modeloTabla.setRowCount(0);
        
        double totalIngresos = 0;
        double totalGastos = 0;
        int contador = 0;
        
        while (rs.next()) {
            String fecha = rs.getString("fecha");
            String tipo = rs.getString("tipo");
            String categoria = rs.getString("categoria");
            String descripcion = rs.getString("descripcion");
            double monto = rs.getDouble("monto");
            String usuario = rs.getString("usuario");
            
            String montoFormateado;
            if (tipo.equalsIgnoreCase("Ingreso")) {
                montoFormateado = "+" + String.format("%.2f", monto) + " US$";
                totalIngresos += monto;
            } else {
                montoFormateado = "-" + String.format("%.2f", monto) + " US$";
                totalGastos += monto;
            }
            
            modeloTabla.addRow(new Object[]{
                fecha,
                tipo,
                categoria,
                descripcion,
                montoFormateado,
                usuario
            });
            
            contador++;
        }
        
        // Actualizar resumen
        lblTotalIngresos.setText(String.format("%.2f US$", totalIngresos));
        lblTotalGastos.setText(String.format("-%.2f US$", totalGastos));
        lblBalance.setText(String.format("%.2f US$", totalIngresos - totalGastos));
        lblTransacciones.setText(String.valueOf(contador));
        
        // Color del balance
        if ((totalIngresos - totalGastos) >= 0) {
            lblBalance.setForeground(new Color(0, 150, 0));
        } else {
            lblBalance.setForeground(new Color(200, 0, 0));
        }
    }
    
    private void limpiarFiltros() {
        comboTipoFiltro.setSelectedIndex(0);
        dateDesde.setDate(null);
        dateHasta.setDate(new java.util.Date());
        comboTipo.setSelectedIndex(0);
        comboAnio.setSelectedIndex(0);
        spinnerDias.setValue(7);
        cargarTodasTransacciones();
    }
    
    private JPanel crearPanelResumen(String titulo, String valor, String tipo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 250, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        if (tipo.equals("green")) {
            lblValor.setForeground(new Color(0, 150, 0));
            lblTotalIngresos = lblValor;
        } else if (tipo.equals("red")) {
            lblValor.setForeground(new Color(200, 0, 0));
            lblTotalGastos = lblValor;
        } else if (tipo.equals("blue")) {
            lblValor.setForeground(new Color(0, 120, 200));
            lblBalance = lblValor;
        } else {
            lblValor.setForeground(new Color(100, 100, 100));
            lblTransacciones = lblValor;
        }
        
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblValor);
        
        return panel;
    }
    
    private String[] generarAnios() {
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        String[] anios = new String[11];
        anios[0] = "Todos";
        for (int i = 0; i < 10; i++) {
            anios[i + 1] = String.valueOf(anioActual - i);
        }
        return anios;
    }
    
    private void cerrarRecursos(ResultSet rs, PreparedStatement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}