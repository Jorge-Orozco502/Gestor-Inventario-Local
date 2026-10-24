package ui;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public class ExportadorReportes {

    private ExportadorReportes() {
    }

    public static void exportarExcel(
            String archivo,
            String resumen,
            JTable... tablas) throws IOException {

        try (Workbook libro = new XSSFWorkbook()) {
            Sheet hojaResumen = libro.createSheet("Resumen");

            hojaResumen.createRow(0)
                    .createCell(0)
                    .setCellValue("Reporte de ventas");

            hojaResumen.createRow(1)
                    .createCell(0)
                    .setCellValue(resumen);

            String[] nombresHojas = {
                "Ventas", "Productos", "Actividad"
            };

            for (int t = 0; t < tablas.length; t++) {
                TableModel modelo = tablas[t].getModel();
                Sheet hoja = libro.createSheet(nombresHojas[t]);

                Row encabezado = hoja.createRow(0);

                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    encabezado.createCell(c)
                            .setCellValue(modelo.getColumnName(c));
                }

                for (int r = 0; r < modelo.getRowCount(); r++) {
                    Row fila = hoja.createRow(r + 1);

                    for (int c = 0; c < modelo.getColumnCount(); c++) {
                        Object valor = modelo.getValueAt(r, c);
                        var celda = fila.createCell(c);

                        if (valor instanceof Number numero) {
                            celda.setCellValue(numero.doubleValue());
                        } else {
                            celda.setCellValue(
                                    valor == null ? "" : valor.toString()
                            );
                        }
                    }
                }

                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    hoja.autoSizeColumn(c);
                }
            }

            try (FileOutputStream salida = new FileOutputStream(archivo)) {
                libro.write(salida);
            }
        }
    }

    public static void exportarPdf(
            String archivo,
            String resumen,
            JTable... tablas) throws IOException, DocumentException {

        Document documento
                = new Document(PageSize.A4.rotate(), 28, 28, 28, 28);

        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            try {
                PdfWriter.getInstance(documento, salida);
                documento.open();

                documento.add(new Paragraph(
                        "Informe de reportes",
                        new Font(Font.HELVETICA, 16, Font.BOLD)
                ));
                documento.add(new Paragraph(resumen));
                documento.add(new Paragraph(" "));

                String[] titulos = {
                    "Ventas por período",
                    "Productos más vendidos",
                    "Actividad reciente"
                };

                for (int t = 0; t < tablas.length; t++) {
                    TableModel modelo = tablas[t].getModel();

                    documento.add(new Paragraph(
                            titulos[t],
                            new Font(Font.HELVETICA, 12, Font.BOLD)
                    ));

                    PdfPTable tabla = new PdfPTable(modelo.getColumnCount());
                    tabla.setWidthPercentage(100);

                    for (int c = 0; c < modelo.getColumnCount(); c++) {
                        tabla.addCell(new Phrase(modelo.getColumnName(c)));
                    }

                    for (int r = 0; r < modelo.getRowCount(); r++) {
                        for (int c = 0; c < modelo.getColumnCount(); c++) {
                            Object valor = modelo.getValueAt(r, c);
                            tabla.addCell(
                                    valor == null ? "" : valor.toString()
                            );
                        }
                    }

                    documento.add(tabla);
                    documento.add(new Paragraph(" "));
                }
            } finally {
                // Cierra y termina el PDF antes de cerrar el archivo de salida.
                if (documento.isOpen()) {
                    documento.close();
                }
            }
        }
    }

    public static void exportarPdfHistorial(
            String archivo,
            String titulo,
            String resumen,
            JTable tablaSwing
    ) throws IOException, DocumentException {

        Document documento = new Document(
                PageSize.A4.rotate(), 28, 28, 28, 28
        );

        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            try {
                PdfWriter.getInstance(documento, salida);
                documento.open();

                documento.add(new Paragraph(
                        titulo,
                        new Font(Font.HELVETICA, 16, Font.BOLD)
                ));
                documento.add(new Paragraph(resumen));
                documento.add(new Paragraph(" "));

                TableModel modelo = tablaSwing.getModel();
                PdfPTable tablaPdf = new PdfPTable(modelo.getColumnCount());
                tablaPdf.setWidthPercentage(100);

                for (int columna = 0;
                        columna < modelo.getColumnCount();
                        columna++) {
                    tablaPdf.addCell(
                            modelo.getColumnName(columna)
                    );
                }

                for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                    for (int columna = 0;
                            columna < modelo.getColumnCount();
                            columna++) {

                        Object valor = modelo.getValueAt(fila, columna);

                        tablaPdf.addCell(
                                valor == null ? "" : valor.toString()
                        );
                    }
                }

                documento.add(tablaPdf);

            } finally {
                if (documento.isOpen()) {
                    documento.close();
                }
            }
        }
    }

}
