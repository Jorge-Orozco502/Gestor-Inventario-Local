
package ui;

import dao.CompraDAO;
import dao.VentaDAO;
import dao.MovimientoInventarioDAO;
import modelo.Compra;
import modelo.Venta;
import modelo.MovimientoInventario;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.util.List;
import java.util.ArrayList;

public class PnlHistorial extends javax.swing.JFrame {
    
    

    
    public PnlHistorial() {
        initComponents();
        inicializarHistorial();
        
        
        DefaultTableModel modeloTablaHistorial = (DefaultTableModel) tblHistorial.getModel();
    
    // 2. Limpiamos cualquier fila anterior
    modeloTablaHistorial.setRowCount(0);
    
    // 3. Llamamos al método pasándole el modelo
    cargarHistorialCompras(modeloTablaHistorial);
    cargarHistorialVentas(modeloTablaHistorial);
        
        
        


modeloTablaHistorial.setRowCount(0);
cargarHistorialCompras(modeloTablaHistorial);
    }
    
    private void cargarHistorialCompras(DefaultTableModel modeloTablaHistorial) {
    CompraDAO compraDAO = new CompraDAO();
    List<Compra> listaCompras = compraDAO.listarCompras();
    
    for (Compra c : listaCompras) {
        Object[] fila = new Object[5];
        
        fila[0] = c.getNumeroCompra();                                  
        fila[1] = c.getFechaCompra() != null ? c.getFechaCompra().toString() : ""; 
        fila[2] = "Compra";                                             
        fila[3] = "$ " + (c.getTotal() != null ? c.getTotal().toString() : "0.00"); 
        fila[4] = "Responsable ID: 1";                                  
        
        modeloTablaHistorial.addRow(fila);
    }
}
    
    private void cargarHistorialVentas(DefaultTableModel modeloTablaHistorial) {
    VentaDAO ventaDAO = new VentaDAO();
    List<Venta> listaVentas = ventaDAO.listarVentas();
    
    for (Venta v : listaVentas) {
        Object[] fila = new Object[5];
        
        fila[0] = v.getNumeroVenta();                                     // ID Registro
        fila[1] = v.getFecha() != null ? v.getFecha().toString() : "";     // Fecha
        fila[2] = "Venta";                                                // Tipo
        fila[3] = "$ " + (v.getTotalFinal() != null ? v.getTotalFinal().toString() : "0.00"); // Total/Cantidad
        fila[4] = v.getUsuarioResponsable() != null ? v.getUsuarioResponsable().getNombreCompleto() : "N/A"; // Responsable
        
        modeloTablaHistorial.addRow(fila);
    }
    }
    
    private DefaultTableModel modeloTablaHistorial;
    private TableRowSorter<DefaultTableModel> sorterHistorial;

    // Llama a esto dentro de tu constructor
    public void inicializarHistorial() {
        configurarTablaHistorial();
        configurarFiltrosYBusqueda();
        cargarTodoElHistorial();
    }

    private void configurarTablaHistorial() {
        String[] columnas = {"ID Registro", "Fecha", "Tipo", "Total/Cantidad", "Usuario Responsable"};
        
        modeloTablaHistorial = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblHistorial.setModel(modeloTablaHistorial);

        sorterHistorial = new TableRowSorter<>(modeloTablaHistorial);
        tblHistorial.setRowSorter(sorterHistorial);
    }

    private void cargarTodoElHistorial() {
        modeloTablaHistorial.setRowCount(0); 
        
        cargarHistorialCompras();
        cargarHistorialVentas();
        cargarHistorialMovimientos();
    }

    private void cargarHistorialCompras() {
        CompraDAO dao = new CompraDAO();
        List<Compra> lista = dao.listarCompras();
        
        for (Compra c : lista) {
            Object[] fila = new Object[5];
            fila[0] = c.getNumeroCompra();
            fila[1] = c.getFechaCompra() != null ? c.getFechaCompra().toString() : "";
            fila[2] = "Compra";
            fila[3] = "$ " + (c.getTotal() != null ? c.getTotal().toString() : "0.00");
            fila[4] = c.getProveedor() != null ? c.getProveedor().getNombreEmpresa() : "Proveedor General";
            modeloTablaHistorial.addRow(fila);
        }
    }

    private void cargarHistorialVentas() {
        VentaDAO dao = new VentaDAO();
        List<Venta> lista = dao.listarVentas();
        
        for (Venta v : lista) {
            Object[] fila = new Object[5];
            fila[0] = v.getNumeroVenta();
            fila[1] = v.getFecha() != null ? v.getFecha().toString() : "";
            fila[2] = "Venta";
            fila[3] = "$ " + (v.getTotalFinal() != null ? v.getTotalFinal().toString() : "0.00");
            fila[4] = v.getUsuarioResponsable() != null ? v.getUsuarioResponsable().getNombreUsuario() : "Sistema";
            modeloTablaHistorial.addRow(fila);
        }
    }

    private void cargarHistorialMovimientos() {
        MovimientoInventarioDAO dao = new MovimientoInventarioDAO();
        List<MovimientoInventario> lista = dao.listarMovimientos();
        
        for (MovimientoInventario m : lista) {
            Object[] fila = new Object[5];
            fila[0] = "MOV-" + m.getIdMovimiento();
            fila[1] = m.getFecha() != null ? m.getFecha().toString() : "";
            fila[2] = m.getTipoMovimiento() != null ? m.getTipoMovimiento().name() : "Inventario";
            fila[3] = m.getCantidad();
            fila[4] = m.getUsuarioResponsable() != null ? m.getUsuarioResponsable().getNombreUsuario() : "Sistema";
            modeloTablaHistorial.addRow(fila);
        }
    }

    private void configurarFiltrosYBusqueda() {
        txtBuscarHistorial.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                aplicarFiltrosCombinados();
            }
        });

        cmbTipoHistorial.addActionListener(e -> aplicarFiltrosCombinados());
    }

    private void aplicarFiltrosCombinados() {
        if (sorterHistorial == null) return;

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String texto = txtBuscarHistorial.getText().trim();
        if (!texto.isEmpty() && !texto.equalsIgnoreCase("Buscar por fecha, referencia o tipo.")) {
            filtros.add(RowFilter.regexFilter("(?i)" + texto));
        }

        String seleccion = cmbTipoHistorial.getSelectedItem() != null ? cmbTipoHistorial.getSelectedItem().toString() : "";
        if (!seleccion.isEmpty() && !seleccion.equalsIgnoreCase("Todos")) {
            if (seleccion.equalsIgnoreCase("Historial de compras")) {
                filtros.add(RowFilter.regexFilter("(?i)^Compra$", 2));
            } else if (seleccion.equalsIgnoreCase("Historial de Ventas")) {
                filtros.add(RowFilter.regexFilter("(?i)^Venta$", 2));
            } else if (seleccion.equalsIgnoreCase("Movimientos de Inventario")) {
                filtros.add(RowFilter.regexFilter("(?i)(ENTRADA|SALIDA|AJUSTE|INVENTARIO)", 2));
            }
        }

        if (!filtros.isEmpty()) {
            sorterHistorial.setRowFilter(RowFilter.andFilter(filtros));
        } else {
            sorterHistorial.setRowFilter(null);
        }
    }
   



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        cmbTipoHistorial = new javax.swing.JComboBox<>();
        btnDescargarPDF = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCerrarSesion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarHistorial = new javax.swing.JTextField();

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Registro", "Fecha", "Tipo", "Total/Cantidad", "Usuario Responsable"
            }
        ));
        tblHistorial.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tblHistorial);
        tblHistorial.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        cmbTipoHistorial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Historial de compras", "Historial de Ventas", "Movimientos de Inventario" }));
        cmbTipoHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoHistorialActionPerformed(evt);
            }
        });

        btnDescargarPDF.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDescargarPDF.setText("Descargar PDF");
        btnDescargarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarPDFActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Tipo de Historial");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Gestión de Historial e Historiales");

        jPanel2.setBackground(new java.awt.Color(51, 255, 255));

        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Sistema de Gestión- Ventanas de PVC");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(340, 340, 340)
                .addComponent(btnCerrarSesion)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        txtBuscarHistorial.setText("Buscar por fecha, referencia o tipo.");
        txtBuscarHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarHistorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(txtBuscarHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbTipoHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnDescargarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnDescargarPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarHistorialActionPerformed
               // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarHistorialActionPerformed

    private void cmbTipoHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoHistorialActionPerformed
               // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoHistorialActionPerformed

    private void btnDescargarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarPDFActionPerformed
        
    }//GEN-LAST:event_btnDescargarPDFActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        
    }//GEN-LAST:event_btnCerrarSesionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDescargarPDF;
    private javax.swing.JComboBox<String> cmbTipoHistorial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JTextField txtBuscarHistorial;
    // End of variables declaration//GEN-END:variables
}
