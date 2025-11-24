package vistas;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class GeneradorPDF {
    
    // Método para descargar Balance General en PDF
    public static void descargarBalancePDF(String contenido, String nombreUsuario, String apellidoUsuario, String rolUsuario, java.awt.Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Balance General como PDF");
        fileChooser.setSelectedFile(new java.io.File("Balance_General.pdf"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showSaveDialog(parent);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();
                
                // Título
                Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph titulo = new Paragraph("Balance General - ContaBook", tituloFont);
                titulo.setAlignment(Element.ALIGN_CENTER);
                document.add(titulo);
                document.add(new Paragraph(" "));
                
                // Datos del usuario
                Font userFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Paragraph usuario = new Paragraph("Generado por: " + nombreUsuario + " " + apellidoUsuario + " (" + rolUsuario + ")", userFont);
                document.add(usuario);
                document.add(new Paragraph("Fecha de descarga: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));
                document.add(new Paragraph(" "));
                
                // Contenido
                Font contentFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                document.add(new Paragraph(contenido, contentFont));
                
                document.close();
                JOptionPane.showMessageDialog(parent, "PDF descargado exitosamente en: " + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Método para descargar Libro Mayor en PDF
    public static void descargarLibroPDF(DefaultTableModel modelo, String nombreUsuario, String apellidoUsuario, String rolUsuario, java.awt.Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Libro Mayor como PDF");
        fileChooser.setSelectedFile(new java.io.File("Libro_Mayor.pdf"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showSaveDialog(parent);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();
                
                // Título
                Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph titulo = new Paragraph("Libro Mayor - ContaBook", tituloFont);
                titulo.setAlignment(Element.ALIGN_CENTER);
                document.add(titulo);
                document.add(new Paragraph(" "));
                
                // Datos del usuario
                Font userFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Paragraph usuario = new Paragraph("Generado por: " + nombreUsuario + " " + apellidoUsuario + " (" + rolUsuario + ")", userFont);
                document.add(usuario);
                document.add(new Paragraph("Fecha de descarga: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));
                document.add(new Paragraph(" "));
                
                // Tabla
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 4, 2, 2, 2});
                
                // Encabezados
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                table.addCell(new PdfPCell(new Phrase("Fecha", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Descripción", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Gasto", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Ingreso", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Saldo", headerFont)));
                
                // Datos
                Font dataFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        table.addCell(new PdfPCell(new Phrase(modelo.getValueAt(i, j).toString(), dataFont)));
                    }
                }
                
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(parent, "PDF descargado exitosamente en: " + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
